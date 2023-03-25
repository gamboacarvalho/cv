package pt.isel

fun parsePerson(json: String, parser: JsonParser) : Person? {
    return parser.parse(json)
}

fun parseDate(json: String, parser: JsonParser) : Date? {
    return parser.parse(json)
}
