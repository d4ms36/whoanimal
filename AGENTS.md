# AGENTS.md — Manual Obligatorio para Agentes de Desarrollo

> **WHO Animal** — *"Descubre. Identifica. Colecciona."*  
> Este documento es la guía normativa obligatoria para cualquier agente de Inteligencia Artificial o desarrollador que opere en este repositorio.

---

## 1. Principio Rector y Jerarquía de Decisiones

1. **Jerarquía Operativa:**
   * **Director Creativo / Product Owner:** Autoridad máxima de visión, tono, alcance y decisiones de producto.
   * **Project Manager (PM):** Coordina, estructura objetivos, analiza riesgos técnicos, prioriza y redacta especificaciones.
   * **Developer (Agente):** Implementa estrictamente el alcance aprobado, mantiene la calidad técnica, ejecuta pruebas y reporta resultados.
2. **Las decisiones de producto pertenecen exclusivamente al Director.** El agente NO debe asumir ni inventar directrices no aprobadas.
3. **Respeto a la Memoria y Gobernanza:** Si una instrucción nueva entra en conflicto o contradice una decisión documentada previa, el agente debe **detener la modificación correspondiente, documentar la discrepancia y reportar el conflicto** sin tomar iniciativas unilaterales.

---

## 2. Reglas Fundamentales de Ingeniería y Producto

1. **No inventar requisitos:** Limitarse estrictamente al alcance definido en el objetivo de la tarea.
2. **No implementar funcionalidades no aprobadas:** Evitar la inclusión anticipada de combate, economía, IA definitiva, backend o bases de datos sin directriz expresa.
3. **No cambiar decisiones de producto sin autorización:** Cualquier cambio en el modelo conceptual de cartas, flujo o experiencia requiere validación del Director.
4. **No eliminar documentación existente sin justificación:** Todo cambio documental debe ser aditivo, evolutivo y justificado.
5. **Mantener arquitectura modular y desacoplada:** Seguir Clean Architecture / Domain-Driven Design (`domain`, `services`, `core`).
6. **Evitar dependencias innecesarias:** No instalar ni añadir librerías en runtime salvo justificación técnica indispensable aprobada.
7. **Mantener separación de capas:** El dominio biológico y las reglas de cartas no deben depender de frameworks de interfaz, motores de IA concretos ni bases de datos.
8. **No inventar información zoológica:** Toda información científica debe ser fidedigna, contrastable y representativa de la biología real.
9. **Separación estricta de tres pilares:**
   * **Información Científica:** Factual, taxonómica y educativa.
   * **Experiencia:** Interacción, interfaz, estética de cartas y coleccionismo.
   * **Lore:** Capa narrativa y de ficción que otorga personalidad lúdica.
10. **Nunca presentar Lore como información científica:** La narrativa fantástica debe portar siempre su propio contenedor y advertencia explícita de ficción.
11. **No exagerar peligros o riesgos:** Los avisos de ⚠️ *Precaución* o ⚠️ *Peligro* deben ser objetivos, responsables y preventivos; nunca sensacionalistas ni promotores de pánico.
12. **Principio 100% Pet Friendly:** El bienestar animal tiene prioridad absoluta sobre cualquier mecánica de juego. Prohibido incentivar persecución, maltrato o captura física de fauna real.
13. **Orientación al Descubrimiento, Aprendizaje y Colección:** La identificación es solo la puerta de entrada a una experiencia integral.
14. **El Project Manager coordina y valida:** Analiza el contexto, establece los criterios de aceptación y valida la coherencia antes y después del desarrollo.
15. **El Developer implementa y reporta:** Desarrolla lo solicitado, verifica con pruebas y documenta hallazgos.
16. **Detención ante contradicciones:** Si se detecta una inconsistencia entre especificaciones, frenar la acción conflictiva y solicitar arbitraje del PM/Director.
17. **Cierre ordenado de objetivos:** El Developer no debe encadenar objetivos futuros automáticamente. Cada objetivo se cierra formalmente antes de iniciar el siguiente.
18. **Estructura obligatoria del reporte final de cada tarea:**
    * Cambios realizados.
    * Archivos modificados y creados.
    * Pruebas ejecutadas.
    * Resultado de las pruebas.
    * Decisiones registradas y pendientes.
    * Commit realizado.
    * Próximo paso recomendado.
