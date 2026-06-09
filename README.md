# 🃏 Mini Proyecto 4 — Yu-Gi-Oh! Simulador MVC

## Integrantes

* Alejandro Jaramillo

---

## Kanban Flow

* https://kanbanflow.com/board/d2ybwUJ

---

# Descripción

Simulador de duelo de Yu-Gi-Oh! desarrollado en Java 21 utilizando programación orientada a objetos, arquitectura MVC, patrones de diseño y persistencia de datos.

El proyecto permite que dos jugadores se enfrenten utilizando monstruos, cartas mágicas y cartas trampa mediante una interfaz gráfica desarrollada con Swing o mediante una versión de consola.

---

# Objetivos del Proyecto

* Aplicar conceptos avanzados de Programación Orientada a Objetos.
* Implementar una arquitectura MVC.
* Utilizar patrones de diseño vistos en clase.
* Aplicar persistencia de información.
* Utilizar estructuras de datos de Java Collections Framework.
* Implementar carga dinámica mediante Reflection.
* Gestionar el desarrollo utilizando GitFlow.

---

# Arquitectura MVC

El proyecto está organizado siguiendo el patrón Modelo Vista Controlador.

## Modelo

Contiene toda la lógica de negocio:

* Juego
* Jugador
* Carta
* Monstruo
* CartaMagica
* CartaTrampa
* Efectos

## Vista

Contiene la interacción con el usuario:

* VentanaDuelo
* PantallaInicio
* vistaConsola
* vistaJuego

## Controlador

Gestiona la comunicación entre la lógica y las vistas:

* controladorJuego

Esta separación permite mantener un código modular, reutilizable y fácil de mantener.

---

# Patrones de Diseño Implementados

## Factory Method

Implementado mediante:

* cartaFactory

Permite reconstruir cartas a partir de su nombre durante los procesos de carga de partidas y restauración de estados.

---

## Reflection Factory

Implementado mediante:

* reflectionFactory

Permite crear dinámicamente cartas mágicas leyendo archivos de configuración externos.

Características:

* Lectura de archivos TXT.
* Carga dinámica de clases.
* Instanciación mediante Reflection.
* Extensibilidad sin modificar código existente.

---

## Memento

Implementado mediante:

* mementoPartida
* caretakerPartida

Permite almacenar estados completos de una partida para mantener historial y recuperación de información.

---

## MVC

Se utiliza como patrón arquitectónico principal para separar responsabilidades entre modelo, vista y controlador.

---

# Persistencia de Datos

El sistema permite guardar y cargar partidas completas.

La información almacenada incluye:

* Turno actual
* Turnos jugados
* Life Points (LP)
* Mano de cada jugador
* Campo de monstruos
* Cementerio
* Cartas restantes del mazo
* Cartas trampa colocadas
* Fecha y hora del guardado

Archivo utilizado:

```text
partida.txt
```

---

# Estadísticas y Reportes

## Registro de Resultados

Cada duelo finalizado genera automáticamente un registro con:

* Fecha
* Jugador 1
* Jugador 2
* Ganador
* Cantidad de turnos
* LP finales

Archivo utilizado:

```text
resultados.txt
```

---

## Ranking de Victorias

El sistema calcula automáticamente el ranking de jugadores utilizando la información almacenada en los resultados.

Las victorias se muestran ordenadas de mayor a menor.

---

## Historial de Guardados

Cada vez que una partida es guardada se registra:

* Fecha
* Hora

Permitiendo llevar seguimiento de los guardados realizados.

---

# Reflection

El proyecto implementa Reflection de Java para cargar dinámicamente efectos de cartas mágicas.

La clase:

```java
reflectionFactory
```

permite:

* Leer archivos de configuración.
* Obtener clases dinámicamente.
* Instanciar efectos en tiempo de ejecución.
* Crear nuevas cartas sin modificar el código fuente principal.

---

# Estructuras de Datos Utilizadas

| Estructura             | Uso                                     |
| ---------------------- | --------------------------------------- |
| ArrayList              | Mano, campo, mazo y cementerio          |
| Stack                  | Historial de estados del patrón Memento |
| HashMap                | Conteo de victorias                     |
| ArrayList + Comparator | Ranking ordenado por victorias          |
| StringBuilder          | Serialización de información            |

---

# Cartas Implementadas (50)

## Monstruos (30)

* Dragon Blanco
* Guerrero
* Mago
* Bestia
* Titan
* Caballero
* Zombie
* Vampiro
* Pitbull
* Mesini
* Esqueleto
* Tarantula
* Gigante
* Ciclope
* Piraña
* Principe
* Principe Oscuro
* Rey Esqueleto
* Caballero Dorado
* Golem Oscuro
* Leon
* Mago Oscuro
* Mago Electrico
* Furia Nocturna
* Duende
* Anguila
* Lombriz Sangrienta
* Araña Aguja
* Sativa
* Golem

---

## Cartas Mágicas (10)

* Pot of Greed
* Curacion Divina
* Orden de Destruccion
* Rayo Oscuro
* Espada de Rafflesia
* Escudo de la Victoria
* Berserker Soul
* Grieta de Poder
* Drenaje de Vida
* Pacto de la Sabiduria

---

## Cartas Trampa (10)

* Trampa Mortal
* Muro de Defensa
* Espejo de Anulacion
* Contraataque Espejo
* Ladron de Almas
* Lagrima del Fénix
* Veneno Oscuro
* Giro del Destino
* Llamada del Cementerio
* Fortaleza Impenetrable

---

# Conceptos OOP Implementados

| Concepto          | Implementación                                  |
| ----------------- | ----------------------------------------------- |
| Clases Abstractas | Carta                                           |
| Herencia          | Monstruo, CartaMagica y CartaTrampa             |
| Interfaces        | Activable y Efecto                              |
| Encapsulamiento   | Atributos privados y getters/setters            |
| Polimorfismo      | Activación dinámica de efectos                  |
| Composición       | Jugador contiene mano, campo, mazo y cementerio |
| MVC               | Separación Modelo-Vista-Controlador             |
| Factory           | cartaFactory                                    |
| Reflection        | reflectionFactory                               |
| Memento           | mementoPartida y caretakerPartida               |
| Colecciones       | ArrayList, Stack, HashMap y Comparator          |

---

# Funcionalidades Implementadas

* Duelos completos entre dos jugadores
* Sistema de combate
* Invocación de monstruos
* Cartas mágicas
* Cartas trampa
* Modo consola
* Interfaz gráfica Swing
* Guardado de partidas
* Carga de partidas
* Historial de guardados
* Ranking de victorias
* Registro de resultados
* Persistencia completa de datos
* Reflection Factory
* Patrón Factory
* Patrón Memento
* Restauración del turno activo
* Restauración del mazo
* Restauración del cementerio
* Restauración de trampas colocadas
* Fecha y hora de guardado

---

# Estructura del Proyecto

```text
CONTROLADOR/
FACTORY/
PATRONES/
PERSISTENCIA/
VISTA/
cartas/
efectos/
jugadores/
juego/
CartasTXT/
```

---

# Gestión del Proyecto

Se utilizó una estrategia basada en GitFlow para el desarrollo.

Ramas utilizadas:

* main
* MP4
* mp3-final
* rama-memento
* rama-persistencia
* rama-reflection

Cada funcionalidad fue desarrollada de forma independiente y posteriormente integrada mediante Pull Requests.

Además, se utilizó un tablero Kanban para el seguimiento de tareas y control del avance del proyecto.

---

# Requisitos

* Java 21 (JDK)
* No requiere librerías externas

---

# Ejecución

1. Ejecutar la clase Main.
2. Seleccionar modo Consola o GUI.
3. Ingresar nombres de los duelistas.
4. Iniciar el duelo.
5. Guardar o cargar partidas cuando se desee.

