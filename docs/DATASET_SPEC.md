# Especificación del Dataset Zoológico (WHO-011A)

**Documento:** `docs/DATASET_SPEC.md`  
**Propósito:** Definir el formato oficial y el contrato estricto para los archivos JSON que actúan como infraestructura de datos (Dataset) de `AnimalProfile` en WHO Animal.

## 1. Reglas Generales

* **Formato:** JSON estricto (`UTF-8`).
* **Inmutabilidad de campos:** No se permiten campos extra (strict validation).
* **Ausencia de inventos:** Todos los datos biológicos deben ser fidedignos.

## 2. Contrato de Campos

Cada entrada de especie en el array de datos debe cumplir con la siguiente estructura:

### Campos Requeridos
* `animal_id` (String): Identificador único de la especie (UUIDv4).
* `scientific_name` (String): Nombre científico completo en latín (no vacío).
* `common_name` (String): Nombre común descriptivo (no vacío).
* `taxonomy` (Objeto): Clasificación taxonómica (debe contener obligatoriamente `kingdom`, `phylum`, `class`, `order`, `family`, `genus`, `species`, sin campos extra, strings no vacíos).

### Campos Opcionales Originales
* `conservation_status` (String): Estado de conservación en texto (ej. "LC", "VU", "EN").
* `is_rare_species` (Boolean): Indica si la especie se considera biológicamente rara en la naturaleza. (Valor booleano estricto, no string).

### Campos Científicos Opcionales (DEC-042)
Estos campos enriquecen el catálogo con información científica estable de la especie:
* `habitat` (String): Descripción del hábitat natural.
* `diet` (String): Tipo de dieta (ej. "Carnívoro", "Herbívoro").
* `lifespan_years` (Integer): Esperanza de vida típica en años.
* `size_cm` (Integer): Tamaño corporal promedio en centímetros.
* `weight_kg` (Number): Peso promedio en kilogramos (entero o flotante).
* `activity_cycle` (String): Ciclo de actividad (ej. "Diurno", "Nocturno").
* `native_regions` (Array of Strings): Lista de regiones geográficas nativas.

Cualquier campo adicional que no esté presente en estas listas será **RECHAZADO**.

## 3. Ejemplo de Documento JSON Válido

```json
[
  {
    "animal_id": "123e4567-e89b-12d3-a456-426614174000",
    "scientific_name": "Panthera onca",
    "common_name": "Jaguar",
    "taxonomy": {
      "kingdom": "Animalia",
      "phylum": "Chordata",
      "class": "Mammalia",
      "order": "Carnivora",
      "family": "Felidae",
      "genus": "Panthera",
      "species": "Panthera onca"
    },
    "conservation_status": "NT",
    "is_rare_species": false,
    "habitat": "Bosques tropicales",
    "diet": "Carnívoro",
    "lifespan_years": 15,
    "size_cm": 170,
    "weight_kg": 95.5,
    "activity_cycle": "Diurno",
    "native_regions": [
      "América del Sur",
      "América Central"
    ]
  }
]
```

## 4. Reglas de Validación
1. **Tipos estrictos:** No se permite coerción automática (ej. `"false"` en vez de `false`).
2. **Strings vacíos:** Los campos requeridos de tipo texto no pueden ser strings vacíos (`""`).
3. **Estructura taxonómica:** El objeto `taxonomy` debe contener exactamente las 7 claves biológicas sin variaciones u omisiones.
4. **Campos ilegales:** Las claves no listadas en el contrato resultarán en un error de validación inmediato.

## 5. Versionado
Actualmente, el dataset se maneja como esquema plano inicial asociado al dominio (`0.0.1`). Cambios en los atributos biológicos requerirán revisión explícita del Director Creativo (aprobación `DEC`).
