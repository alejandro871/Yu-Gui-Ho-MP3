# 🃏 Mini Proyecto 4 — Yu-Gi-Oh! Simulador GUI

## Integrantes

* Alejandro Jaramillo

---

## Descripción

Simulador de duelo de Yu-Gi-Oh! desarrollado en Java 21 con interfaz gráfica usando **Swing**.
Permite que dos jugadores se enfrenten usando monstruos, cartas mágicas y cartas trampa,
todo a través de una interfaz visual con fondo oscuro temático.

---
## Patrones de Diseño Implementados

### Factory Method
Permite reconstruir cartas a partir de su nombre utilizando una fábrica centralizada (`cartaFactory`).

### Reflection Factory
Permite crear dinámicamente cartas mágicas leyendo archivos de configuración y cargando sus efectos mediante Reflection.

### Memento
Permite almacenar instantáneas del estado de una partida para mantener historial de guardados.

### MVC (Modelo Vista Controlador)
El proyecto está organizado siguiendo el patrón MVC para separar lógica de negocio, interfaz gráfica y controladores.


## Instrucciones de ejecución

### Requisitos
- Java 21 (JDK)
- No se requieren librerías externas

## Cómo se juega

1. Al iniciar aparece la **Pantalla de Inicio**: ingresa los nombres de los dos duelistas
2. El azar decide quién empieza
3. Cada turno:
   - Presiona **" Robar Carta"** al inicio del turno
   - Haz clic en una carta de tu mano (panel inferior) para jugarla
   - Usa el botón **" Atacar"** para declarar ataques con tus monstruos
   - Cuando el enemigo ataca, verás un diálogo preguntándote si quieres activar trampas
   - Presiona **" Terminar Turno"** para pasar al siguiente jugador
4. Gana quien reduzca los LP del rival a 0 o lo haga quedarse sin cartas en el mazo

---

## Persistencia de Partidas

El sistema permite guardar y cargar partidas completas.

La información almacenada incluye:

- Turno actual
- Life Points (LP)
- Mano de cada jugador
- Campo de monstruos
- Cementerio
- Cartas restantes del mazo
- Cartas trampa colocadas
- Fecha y hora del guardado

Las partidas se almacenan en el archivo:

partida.txt


---

# 4. Agregar Historial y Ranking

```md
## Estadísticas

El sistema registra automáticamente:

### Ranking de Victorias
Se almacena la cantidad de victorias obtenidas por cada jugador.

### Historial de Guardados
Se registra la fecha y hora de cada guardado realizado durante las partidas.


## Cartas implementadas (50 en total)

### 30 Monstruos
Dragon Blanco, Guerrero, Mago, Bestia, Titan, Caballero, Zombie, Vampiro,
Pitbull, Mesini, Esqueleto, Tarantula, Gigante, Ciclope, Piraña, Principe,
Principe Oscuro, Rey Esqueleto, Caballero Dorado, Golem Oscuro, Leon,
Mago Oscuro, Mago Electrico, Furia Nocturna, Duende, Anguila, Lombriz Sangrienta,
Araña Aguja, Sativa, Golem

### 10 Cartas Mágicas
Pot of Greed, Curacion Divina, Orden de Destruccion, Rayo Oscuro,
Espada de Rafflesia, Escudo de la Victoria, Berserker Soul, Grieta de Poder,
Drenaje de Vida, Pacto de la Sabiduria

### 10 Cartas Trampa
Trampa Mortal, Muro de Defensa, Espejo de Anulacion, Contraataque Espejo,
Ladron de Almas, Lagrima del Fénix, Veneno Oscuro, Giro del Destino,
Llamada del Cementerio, Fortaleza Impenetrable

---

## Conceptos OOP implementados

| Concepto | Implementación |
|-----------|-----------|
| Clases abstractas | Carta |
| Herencia | Monstruo, CartaMagica y CartaTrampa |
| Interfaces | Activable y Efecto |
| Encapsulamiento | Atributos privados y getters/setters |
| Polimorfismo | Activación dinámica de efectos |
| Composición | Jugador contiene mano, campo, mazo y cementerio |
| MVC | Separación Modelo-Vista-Controlador |
| Factory | cartaFactory |
| Reflection | reflectionFactory |
| Memento | mementoPartida y caretakerPartida |
| Colecciones | ArrayList, Stack, HashMap y TreeMap |

---

## Estructura del Proyecto

CONTROLADOR/
FACTORY/
MODELO/
PATRONES/
PERSISTENCIA/
VISTA/
CartasTXT/

---

## Funcionalidades Implementadas

- Duelos completos entre dos jugadores
- Invocación de monstruos
- Cartas mágicas
- Cartas trampa
- Sistema de combate
- Persistencia de partidas
- Historial de guardados
- Ranking de victorias
- Reflection para carga dinámica de efectos
- Patrón Factory
- Patrón Memento
- Interfaz gráfica con Swing
- Modo consola
