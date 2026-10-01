package com.example.healthclinic.application.service;

import com.example.healthclinic.application.dto.CreatePatientCommand;
import com.example.healthclinic.application.dto.PatientResponse;
import com.example.healthclinic.application.mapper.PatientDtoMapper;
import com.example.healthclinic.application.port.in.CreatePatientUseCase;
import com.example.healthclinic.application.port.in.GetPatientUseCase;
import com.example.healthclinic.application.port.out.LoadPatientPort;
import com.example.healthclinic.application.port.out.SavePatientPort;
import com.example.healthclinic.application.port.out.PatientEventPublisherPort;
import com.example.healthclinic.domain.exception.PatientNotFoundException;
import com.example.healthclinic.domain.model.Patient;
import com.example.healthclinic.domain.model.PatientId;
import com.example.healthclinic.domain.model.PatientStatus;
import com.example.healthclinic.domain.service.PatientPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.core.util.Ids;

/**
 * SERVICIO DE APLICACION: orquesta, no decide.
 *
 * <p>Implementa los puertos de entrada y solo habla con puertos de salida.
 * No lleva ni una anotacion de framework: se expone como bean CDI desde
 * {@code infrastructure.config.PatientBeanConfiguration}. El precio son
 * cinco lineas de configuracion; el beneficio es que estos casos de uso se
 * testean con un {@code new} y sin arrancar Quarkus.</p>
 */
public class PatientService implements GetPatientUseCase, CreatePatientUseCase {

    private final LoadPatientPort loadPatientPort;
    private final SavePatientPort savePatientPort;
    private final PatientEventPublisherPort eventPublisherPort;
    private final PatientPolicy policy;
    private final TimeProvider timeProvider;

    public PatientService(LoadPatientPort loadPatientPort,
                             SavePatientPort savePatientPort,
                             PatientEventPublisherPort eventPublisherPort,
                             PatientPolicy policy,
                             TimeProvider timeProvider) {
        this.loadPatientPort = loadPatientPort;
        this.savePatientPort = savePatientPort;
        this.eventPublisherPort = eventPublisherPort;
        this.policy = policy;
        this.timeProvider = timeProvider;
    }

    @Override
    public PatientResponse getById(PatientId id) {
        Patient aggregate = loadPatientPort.loadById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        policy.ensureUsable(aggregate);
        return PatientDtoMapper.toResponse(aggregate);
    }

    @Override
    public PatientResponse create(CreatePatientCommand command) {
        policy.validateForCreation(command.label(), command.copaymentAmount());

        Patient aggregate = new Patient(
                PatientId.of(Ids.newId()),
                command.label(),
                PatientStatus.ACTIVE,
                command.copaymentAmount(),
                timeProvider.now());

        Patient saved = savePatientPort.save(aggregate);
        eventPublisherPort.publishCreated(saved);
        return PatientDtoMapper.toResponse(saved);
    }
}
