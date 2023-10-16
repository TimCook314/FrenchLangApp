//fun main() {
//    println("Hello, World!")
//}

import java.io.BufferedReader
import java.io.File
import java.io.FileReader


// ---------------------------

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    readVocabData("G:\\My Drive\\French\\A_NewVocab.txt")
    speakIt()
    if ((0..1).random() > 20) {
        readVocabData("G:\\My Drive\\French\\TestVocab2.txt")
    }
}


private fun reportIt(txt: String) {
    println(txt)
}

private fun speakIt() {
    var itemsPlayed = 0
    while (itemsPlayed < 10) {
        itemsPlayed++
        val fAndEPair = getThePhrase()
        val toPrint = "${fAndEPair.fTxt} --> ${fAndEPair.eTxt}"
        reportIt(toPrint)
    }
}

//! private fun readVocalData(context: Context, uri: Uri, name: String) {
private fun readVocabData( name: String) {
    if (name == "none") {
        return
    }
    println("Reading file.")
    try {
        val file = File(name)
        val bufferedReader = BufferedReader(FileReader(file))
        val linesRead = readIt(bufferedReader)
        reportIt("  Lines read is $linesRead")
        // Close the BufferedReader
        bufferedReader.close()
    } catch (e: Exception) {
        reportIt("*** Error accessing file.")
    }
}


//fun addTheToStartOfNoun( noun: String, mf: String ) {
//    var startsWithVowel = false
//
//    var char1 = noun.lowercase()[0]
//    if (char1 == 'h') {
//        //use second character
//        char1 = noun.lowercase()[1]
//    }
//    when (char1) {
//        'a' -> { startsWithVowel = true }
//        'e' -> { startsWithVowel = true }
//        'i' -> { startsWithVowel = true }
//        'o' -> { startsWithVowel = true }
//        'u' -> { startsWithVowel = true }
//        'é' -> { startsWithVowel = true }
//        'à' -> { startsWithVowel = true }
//        'è' -> { startsWithVowel = true }
//        'ù' -> { startsWithVowel = true }
//        'â' -> { startsWithVowel = true }
//        'ê' -> { startsWithVowel = true }
//        'î' -> { startsWithVowel = true }
//        'ô' -> { startsWithVowel = true }
//        'û' -> { startsWithVowel = true }
//        'ë' -> { startsWithVowel = true }
//        'ï' -> { startsWithVowel = true }
//        'ü' -> { startsWithVowel = true }
//    }
//}

// --------------------


//Classes -----
enum class Perspective {
    Unset, First, Second, Third,
}

enum class Gender {
    Unset, M, F, Either,
}
enum class Quantity {
    Unset, Single, Plural, Either,
}


class Pronoun {
    var MFSP: String = ""
    var fText: String = ""
    var eText: String = ""
    var freq: Int = 1
}

class Noun {
    var wordGender: Gender = Gender.Unset
    var wordQuantity: Quantity = Quantity.Unset
    var article: String = ""
    var sText: String = ""
    var pText: String = ""
    var eText: String = ""
    //var freq: Int = 1
}

class Adjective {
    var eAdj: String = ""
    var relPos: String = ""
    var masculine: String = ""
    var feminine: String = ""
    var masculinePlural: String = ""
    var femininePlural: String = ""
    var freq: Int = 1
}

class Verb {
    var fInfinitive: String = ""
    var eBase: String = ""
    var e3rdPeronSingular: String = ""
    var Je: String = ""
    var Tu: String = ""
    var Il_Elle: String = ""
    var Nous: String = ""
    var Vous: String = ""
    var Ils_Elles: String = ""
    var freq: Int = 1
}


class Phrase {
    var fPhrase: String = ""
    var ePhrase: String = ""
    var freq: Int = 1
}

class LangData {
    var frenchSpeed: Float = 1.0f
    var frenchPauseFactor: Double = 1.0
    var phrasePatterns = mutableListOf<String>()
    var audioPattern: String = "fef"
    var frenchVoice: String = ""
    var englishVoice: String = ""

    val pronoun = mutableListOf<Pronoun>()
    val nouns = mutableListOf<Noun>()
    val adjectives = mutableListOf<Adjective>()
    val verbs = mutableListOf<Verb>()
    val phrases = mutableListOf<Phrase>()

}

data class FAndEPair(val fTxt: String, val eTxt: String)
data class ANoun(val pqg: PQG, val article: String, val fTxt: String, val eTxt: String)


class PQG {
    var perspective: Perspective = Perspective.Unset
    var gender: Gender = Gender.Unset
    var quantity: Quantity = Quantity.Unset
}

class AnAdjective {
    var eText: String = ""
    var fText: String = ""
    var gender: Gender = Gender.M
    var quantity: Quantity = Quantity.Single
}

class Subject {
    var eText: String = ""
    var fText: String = ""
    var pqg: PQG = PQG()
}

//
//enum class SubjectPerspective {
//    Je_Nous, sdf, WEST, EAST
//}
//
//class SubjectPart {
//    var eTxt: String = ""
//    var fTxt: String = ""
//    var perspective: SubjectPerspective = SubjectPerspective.Je_Nous
//    var quantity: SubjectQuantity = SubjectQuantity.single
//    var gender: SubjectGender = SubjectGender.m
//}


//Data ------
private var vocabList: LangData = LangData()

//Code  ------------




fun readIt(bufferedReader: BufferedReader): Int {
    var linesRead = 0
    try {
        // Use a loop to read each line until the end of the file
        var line: String?
        do {
            line = bufferedReader.readLine()
            linesRead++
            if (line == null) {
                break
            }
            line = line.trim()
            if (line.startsWith("#")) {
                continue
            } else if (line.startsWith("@")) {
                //code it
                val parts = line.split("=")
                val part0 = parts[0].drop(1).trim()
                when (part0) {
                    "frenchSpeed" -> {
                        vocabList.frenchSpeed = parts[1].toFloat()
                    }
                    "frenchPauseFactor" -> {
                        vocabList.frenchPauseFactor = parts[1].toDouble()
                    }
                    "phrasePatterns" -> {
                        vocabList.phrasePatterns.add( parts[1].trim() )
                    }
                    "audioPattern" -> {
                        vocabList.audioPattern = parts[1].trim()
                    }
                    "frenchVoice" -> {
                        vocabList.frenchVoice = parts[1].trim()
                    }
                    "englishVoice" -> {
                        vocabList.englishVoice = parts[1].trim()
                    }
                    else -> {
                        reportIt("*** Error parsing line $line")
                        //code
                    }
                }
            } else {
                val parts = line.split(";")
                val part0 = parts[0].lowercase().trim()
                when (part0) {
                    "pronoun" -> {
                        val the = Pronoun()
                        the.fText = parts[1].trim()
                        the.MFSP = parts[2].trim()
                        the.eText = parts[3].trim()
                        if (parts.size >= (4 + 1)) {
                            the.freq = parts[4].toInt()
                        }
                        vocabList.pronoun.add(the)
                    }

                    "noun" -> {
                        val the = Noun()
                        the.wordGender = stringToGender(parts[1].trim())
                        the.wordQuantity = stringToQuantity(parts[2].trim())
                        the.article = parts[3].trim()
                        val txt1: String = parts[4].trim()
                        val iStart = txt1.indexOf('(')
                        if (iStart < 0) {
                            the.sText = txt1
                            the.pText = txt1
                        } else {
                            val iEnd = txt1.indexOf(')')
                            val sForm = txt1.substring(0,iStart)
                            var pForm = txt1.substring(iStart+1,iEnd)
                            if (pForm.length <= 1) {
                                pForm = sForm + pForm
                            }
                            the.sText = sForm
                            the.pText = pForm
                        }
                        the.eText = parts[5].trim()
                        vocabList.nouns.add(the)
                    }

                    "adjective" -> {
                        val the = Adjective()
                        the.eAdj = parts[1].trim()
                        the.relPos = parts[2].trim()
                        the.masculine = parts[3].trim()
                        the.feminine = parts[4].trim()
                        the.masculinePlural = parts[5].trim()
                        the.femininePlural = parts[6].trim()
                        if (parts.size >= (7 + 1)) {
                            the.freq = parts[7].toInt()
                        }
                        vocabList.adjectives.add(the)
                    }

                    "verb" -> {
                        val the = Verb()
                        the.fInfinitive = parts[1].trim()
                        the.eBase = parts[2].trim()
                        the.e3rdPeronSingular = parts[3].trim()
                        the.Je = parts[4].trim()
                        the.Tu = parts[5].trim()
                        the.Il_Elle = parts[6].trim()
                        the.Nous = parts[7].trim()
                        the.Vous = parts[8].trim()
                        the.Ils_Elles = parts[9].trim()
                        if (parts.size >= (10 + 1)) {
                            the.freq = parts[10].toInt()
                        }
                        vocabList.verbs.add(the)
                    }

                    "phrase" -> {
                        val the = Phrase()
                        the.fPhrase = parts[1].trim()
                        the.ePhrase = parts[2].trim()
                        if (parts.size >= (3 + 1)) {
                            the.freq = parts[3].toInt()
                        }
                        vocabList.phrases.add(the)
                    }

                    else -> {
                        //code
                    }
                }
            }
        } while (true) // (line != null)
    } catch (e: Exception) {
        reportIt("*** Error reading file at about line $linesRead")
    }
    return linesRead
}

private fun getThePhrase(): FAndEPair {
    var fText1 = "Erreur!"
    var eText1 = "Error!"
    //Decide what to do next
    val pattern = vocabList.phrasePatterns.random()
    when (pattern) {
        "phrase" -> {
            val phrase = vocabList.phrases.random()
            fText1 = phrase.fPhrase
            eText1 = phrase.ePhrase

        }
        "noun" -> {
            val aNoun = getAnyNoun()
            fText1 = aNoun.fTxt
            eText1 = aNoun.eTxt
        }
        "the noun" -> {
            val aNoun = getAnyNoun()
            fText1 = if (aNoun.article.endsWith("'")) {
                aNoun.article + aNoun.fTxt
            } else {
                aNoun.article + " " + aNoun.fTxt
            }
            eText1 = "the ${aNoun.eTxt}"
        }
        "Je suis {adjective.Masculine or adjective.Feminine}" -> {
            val subjectGender: Gender = Gender.values().random()
            val subjectQuantity: Quantity = Quantity.Single
            val adj2 = getAnAdjective( subjectGender, subjectQuantity )
                fText1 = "Je suis ${adj2.fText}"
                eText1 = "I am ${adj2.eText}"
        }
        "{pronoun} are {adjective}" -> {
            val pronouns = listOf("Je", "Tu", "Il", "Elle", "Nous", "Vous", "Ils", "Elles")
            val pronoun = pronouns.random()
            when (pronoun) {
                "Je" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Je suis ${adj2.fText}"
                    eText1 = "I am ${adj2.eText}"
                }

                "Tu" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Tu es ${adj2.fText}"
                    eText1 = "You are ${adj2.eText}"
                }

                "Il" -> {
                    val subjectGender: Gender = Gender.M
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Il est ${adj2.fText}"
                    eText1 = "He is ${adj2.eText}"
                }

                "Elle" -> {
                    val subjectGender: Gender = Gender.F
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Elle est ${adj2.fText}"
                    eText1 = "She is ${adj2.eText}"
                }

                "Nous" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.Plural
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Nous sommes ${adj2.fText}"
                    eText1 = "We are ${adj2.eText}"
                }

                "Vous" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.values().random()
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Vous êtes ${adj2.fText}"
                    eText1 = if (subjectQuantity == Quantity.Single) {
                        "You are ${adj2.eText}"
                    } else {
                        "Y'all are ${adj2.eText}"
                    }
                }

                "Ils" -> {
                    val subjectGender: Gender = Gender.M
                    val subjectQuantity: Quantity = Quantity.Plural
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Ils sont ${adj2.fText}"
                    eText1 = "They are ${adj2.eText}"
                }

                "Elles" -> {
                    val subjectGender: Gender = Gender.F
                    val subjectQuantity: Quantity = Quantity.Plural
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Elles sont ${adj2.fText}"
                    eText1 = "They are ${adj2.eText}"
                }
            }
        }
        "{pronoun} {want/have/like/go/take} {a/the} {noun}" -> {
            //Get a subject pronoun
            //Get a verb
            //Get a or the
            //Get object (noun for now)

            //val subject = getASubject()
            //val aVerb = getAVerb( PQG )
            //val anObject = getAnObject(  )


            val pronouns = listOf("Je", "Tu", "Il", "Elle", "Nous", "Vous", "Ils", "Elles")
            val pronoun = pronouns.random()
        }
    }
    return FAndEPair(fText1, eText1)
}

private fun getAnAdjective(gender: Gender, quantity: Quantity): AnAdjective {
    val anAdjective = AnAdjective()
    val adj = vocabList.adjectives.random()
    anAdjective.gender = gender
    anAdjective.quantity = quantity
    if ( gender == Gender.M ) {
        if ( quantity == Quantity.Single) {
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.masculine
        } else { //quantity == Quantity.plural
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.masculinePlural
        }
    } else { //gender == Gender.f ) {
        if ( quantity == Quantity.Single) {
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.feminine
        } else { //quantity == Quantity.plural
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.femininePlural
        }
    }
    if ((0..4).random() >=4) {
        anAdjective.eText = "very " + anAdjective.eText
        anAdjective.fText = "très " + anAdjective.fText
    }
    return anAdjective
}

//private fun getASubject(): Subject {
//    var aSubject = Subject()
//
//    if ((0..4).random() >= 3) {
//        val noun = vocabList.nouns.random()
//        //TODO: Select if we are going to use a or the in front
//        //TODO: Select if we are going to make it plural
//        aSubject.eText = noun.eText
//        aSubject.fText = noun.fText
//        aSubject.pqg.gender = Gender.M
//        aSubject.pqg.perspective = Perspective.First //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.quantity = Quantity.Single //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.gender = Gender.M //TODO: Fix  Need a function to select or get the gender from MFSP
//    } else {
//        val pronoun = vocabList.pronoun.random()
//        aSubject.eText = pronoun.eText
//        aSubject.eText = pronoun.fText
//        aSubject.pqg.perspective = Perspective.First //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.quantity = Quantity.Single //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.gender = Gender.M //TODO: Fix  Need a function to select or get the gender from MFSP
//    }
//    return aSubject
//}


private fun getAnyNoun(): ANoun {
    //data class ANoun(val pqg: PQG, val article: String, val fTxt: String, val eTxt: String)
    val pqg: PQG = PQG()
    var article: String = ""
    var fTxt: String = "Erreur"
    var eTxt: String = "Error"
    val noun = vocabList.nouns.random()
    pqg.quantity = noun.wordQuantity
    if (pqg.quantity == Quantity.Either) {
        pqg.quantity = Quantity.Single
        if ((0..2).random() >= 2) {
            pqg.quantity = Quantity.Plural
        }
    }
    if (pqg.quantity == Quantity.Single) {
        pqg.quantity = Quantity.Single
        pqg.gender = noun.wordGender
        pqg.perspective = Perspective.Third
        article = noun.article
        fTxt = noun.sText
        eTxt = noun.eText
    } else if (pqg.quantity == Quantity.Plural) {
        pqg.quantity = Quantity.Plural
        pqg.gender = noun.wordGender
        pqg.perspective = Perspective.Third
        article = "les"
        fTxt = noun.pText
        eTxt = noun.eText + "s"
    }
    return ANoun(pqg, article, fTxt, eTxt)
}
private fun stringToGender(txt: String): Gender {
    val txt2 = txt.trim().lowercase()
    if (txt2 == "m") {
        return Gender.M
    } else if (txt2 == "f") {
        return Gender.F
    }
    return Gender.Unset
}
private fun stringToQuantity(txt: String): Quantity {
    val txt2 = txt.trim().lowercase()
    if (txt2 == "s") {
        return Quantity.Single
    } else if (txt2 == "p") {
        return Quantity.Plural
    } else if (txt2 == "s&p") {
        return Quantity.Either
    } else if (txt2 == "s(p)") {
        return Quantity.Either
    }
    return Quantity.Unset
}

//private fun getArtial
//    var subjectPart: SubjectPart = SubjectPart()
//
//    subjectPart.gender = SubjectGender.m
//
//    return subjectPart
//}