package pt.isel

import pt.isel.autorouter.autorouterReflect
import pt.isel.autorouter.jsonServer

fun main() {
    ClassroomRouter().autorouterReflect().jsonServer().start(4000)
}
