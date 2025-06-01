package others


// Data class para representar un Técnico
data class Tecnico(
    var id: String = "", // ID único del técnico, Firestore puede generarlo
    var nombres: String = "",
    var apellidos: String = "",
    var identificacion: String = "",
    var cargo: String = "",
    var telefono: String = "",
    var correo: String = "",
    var direccion: String = "",
    var fechaNacimiento: String = "" // Guardaremos la fecha como String por ahora
    // Podrías considerar guardar fechaNacimiento como Long (timestamp) o usar objetos Date de Firebase
) {
    // Constructor sin argumentos requerido por Firestore para deserializar
    constructor() : this("", "", "", "", "", "", "", "", "")
}