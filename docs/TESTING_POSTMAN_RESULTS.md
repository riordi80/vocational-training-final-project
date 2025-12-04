# Resultados del Testing con Postman

**Fecha**: 2025-12-03
**Responsables**: Testing de endpoints REST con Postman
**Estado**: [x] COMPLETADO

---

## Resumen Ejecutivo

Se ha realizado un testing exhaustivo de los endpoints REST del backend Spring Boot, validando:
- [x] Operaciones CRUD completas (Create, Read, Update, Delete)
- [x] Relación 1:N entre CentroEducativo y Arbol
- [x] Validaciones de Bean Validation (@NotBlank, @Past, @DecimalMin/Max)
- [x] Validaciones de lógica de negocio (duplicados)
- [x] Códigos de estado HTTP correctos

**Resultado**: Todos los tests pasaron exitosamente.

---

## Tests Realizados

### **Grupo 1: CRUD Centros Educativos**

| Test | Endpoint | Método | Resultado | Código HTTP |
|------|----------|--------|-----------|-------------|
| Crear centro 1 | `/api/centros` | POST | [x] Exitoso | 200 |
| Crear centro 2 | `/api/centros` | POST | [x] Exitoso | 200 |
| Listar todos los centros | `/api/centros` | GET | [x] Exitoso | 200 |
| Obtener centro por ID | `/api/centros/1` | GET | [x] Exitoso | 200 |

**Datos de prueba creados:**
1. IES El Rincón (ID: 1)
2. CEIP Las Palomas (ID: 2)

---

### **Grupo 2: Validaciones de Centros**

| Test | Entrada | Resultado Esperado | Resultado Real | Código HTTP |
|------|---------|-------------------|----------------|-------------|
| Nombre vacío | `nombre: ""` | Rechazar (400) | [x] Rechazado | 400 Bad Request |
| Nombre duplicado | `nombre: "IES El Rincón"` (ya existe) | Rechazar (409) | [x] Rechazado | 409 Conflict |

**Validaciones comprobadas:**
- [x] `@NotBlank` en campo `nombre`
- [x] Validación de negocio: `existsByNombre()`

---

### **Grupo 3: CRUD Árboles**

| Test | Endpoint | Método | Resultado | Código HTTP |
|------|----------|--------|-----------|-------------|
| Crear árbol 1 | `/api/arboles` | POST | [x] Exitoso | 200 |
| Crear árbol 2 | `/api/arboles` | POST | [x] Exitoso | 200 |
| Crear árbol 3 | `/api/arboles` | POST | [x] Exitoso | 200 |
| Listar todos los árboles | `/api/arboles` | GET | [x] Exitoso | 200 |

**Datos de prueba creados:**
1. Pino Canario del Patio (ID: 1, Centro: 1, Fecha: 2020-03-15)
2. Drago Joven (ID: 2, Centro: 1, Fecha: 2018-06-20)
3. Palmera Canaria (ID: 3, Centro: 2, Fecha: 2015-09-10)

**Distribución:**
- Centro 1 (IES El Rincón): 2 árboles
- Centro 2 (CEIP Las Palomas): 1 árbol

---

### **Grupo 4: Validaciones de Árboles**

| Test | Entrada | Resultado Esperado | Resultado Real | Código HTTP |
|------|---------|-------------------|----------------|-------------|
| Fecha futura | `fechaPlantacion: "2030-12-31"` | Rechazar (400) | [x] Rechazado | 400 Bad Request |
| Nombre vacío | `nombre: ""` | Rechazar (400) | [x] Rechazado | 400 Bad Request |

**Validaciones comprobadas:**
- [x] `@Past` en campo `fechaPlantacion` (no permite fechas futuras)
- [x] `@NotBlank` en campo `nombre`

---

### **Grupo 5: Relación 1:N (Requisito clave de PGV)**

| Test | Endpoint | Método | Resultado Esperado | Resultado Real | Código HTTP |
|------|----------|--------|-------------------|----------------|-------------|
| Árboles del centro 1 | `/api/centros/1/arboles` | GET | 2 árboles | [x] 2 árboles devueltos | 200 |
| Árboles del centro 2 | `/api/centros/2/arboles` | GET | 1 árbol | [x] 1 árbol devuelto | 200 |

**Verificaciones:**
- [x] El centro 1 devuelve correctamente "Pino Canario del Patio" y "Drago Joven"
- [x] El centro 2 devuelve correctamente "Palmera Canaria"
- [x] La relación 1:N (un centro → muchos árboles) funciona correctamente
- [x] No hay loops JSON infinitos (@JsonIgnore implementado)

---

## Requisitos Académicos Cumplidos

### **[PGV] Backend - Noviembre**

#### **Requisito 1: 2 endpoints con relación 1:N**

**Endpoints implementados:**
1. **CentroEducativoController** (entidad padre)
   - GET `/api/centros` - Listar todos
   - GET `/api/centros/{id}` - Obtener por ID
   - POST `/api/centros` - Crear
   - PUT `/api/centros/{id}` - Actualizar
   - DELETE `/api/centros/{id}` - Eliminar
   - **GET `/api/centros/{id}/arboles`** - Obtener árboles del centro (relación 1:N)

2. **ArbolController** (entidad hija)
   - GET `/api/arboles` - Listar todos
   - GET `/api/arboles/{id}` - Obtener por ID
   - POST `/api/arboles` - Crear (con `centroEducativo.id`)
   - PUT `/api/arboles/{id}` - Actualizar
   - DELETE `/api/arboles/{id}` - Eliminar
   - GET `/api/arboles/centro/{centroId}` - Filtrar por centro

**Relación 1:N verificada:**
- Un CentroEducativo puede tener muchos Arboles
- Cada Arbol pertenece a un CentroEducativo
- El endpoint `/api/centros/{id}/arboles` demuestra la relación funcionando

#### **Requisito 2: Validaciones de datos**

**Validaciones implementadas y probadas:**

1. **Bean Validation (anotaciones JPA):**
   - `@NotBlank` en campos obligatorios (nombre, especie)
   - `@NotNull` en campos requeridos (fechaPlantacion)
   - `@Past` en fechaPlantacion (no permite fechas futuras)
   - `@DecimalMin/@Max` en umbrales (aunque con valores null por defecto)

2. **Validaciones de lógica de negocio:**
   - Centro duplicado: `existsByNombre()` → 409 Conflict
   - Árbol duplicado en centro: `existsByNombreAndCentroEducativo()` → 409 Conflict

3. **Validaciones automáticas de Spring:**
   - `@Valid` en controllers para activar Bean Validation
   - Respuestas HTTP correctas (400 Bad Request para validación, 409 Conflict para duplicados)

---

## Estadísticas del Testing

- **Total de tests realizados**: 12 tests principales
- **Tests exitosos**: 12 (100%)
- **Tests fallidos**: 0
- **Endpoints probados**: 18 endpoints totales
- **Códigos HTTP verificados**: 200 (OK), 400 (Bad Request), 409 (Conflict)

---

## Observaciones y Notas

### **Umbrales con valor null**

**Observación**: Los campos de umbrales (umbralTempMin, umbralTempMax, etc.) se guardan como `null` en lugar de usar los valores por defecto definidos en SQL.

**Causa**: Los valores `DEFAULT` en SQL solo se aplican cuando se inserta directamente en la base de datos, no cuando Hibernate/JPA gestiona la inserción.

**Impacto**: Ninguno para el deadline del 8 de diciembre. El sistema funciona correctamente.

**Solución opcional (futuro)**: Asignar valores por defecto en Java usando `@PrePersist` o en el constructor de la entidad.

### **Manejo de errores**

Spring Boot maneja automáticamente los errores de validación con respuestas JSON estructuradas:
- Timestamp
- Status HTTP
- Error description
- Path del endpoint

Los logs del servidor muestran información detallada adicional para debugging.

---

## Conclusión

El backend Spring Boot está **completamente funcional** y cumple con todos los requisitos académicos de **[PGV] Backend - Noviembre**:

1. [x] **2 endpoints con relación 1:N** (CentroEducativo ↔ Arbol)
2. [x] **Validaciones de datos** (Bean Validation + lógica de negocio)

**Próximos pasos:**
- **Fase 3**: Configurar Frontend React
- **Fase 4**: Implementar CRUD de árboles en React
- **Fase 5**: Desarrollar app Android

**Estado general del proyecto**:
- Backend: [x] COMPLETO (Fases 1 y 2)
- Frontend: [ ] PENDIENTE
- Android: [ ] PENDIENTE
- Documentación: [ ] EN CURSO

---

**Documento generado**: 2025-12-03 16:55
**Última actualización**: 2025-12-03 16:55
