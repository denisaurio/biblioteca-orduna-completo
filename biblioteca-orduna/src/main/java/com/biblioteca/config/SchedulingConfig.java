package com.biblioteca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Activa las tareas automáticas (@Scheduled) de tarea/TareasProgramadas. */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
