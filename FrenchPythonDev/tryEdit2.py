

from pydub import AudioSegment


data = [
    ("addition, l'addition, une addition", "check"),
    ("aéroport, l'aéroport, un aéroport", "aeroport"),
    ("amie, l'amie, une amie", "friend"),
    ("angleterre, l'angleterre, une angleterre", "England"),
    ("animal de compagnie, l'animal de compagnie, un animal de compagnie", "pet"),
    ("appartement, l'appartement, un appartement", "apartment"),
    ("après-midi, l'après-midi, un après-midi", "afternoon"),
    ("arbre, l'arbre, un arbre", "tree"),
    ("arbres, les arbres, des arbres", "trees"),
    ("avion, l'avion, un avion", "airplane"),
    ("avions, les avions, des avions", "airplanes"),
    ("bananes, les bananes, des bananes", "bananas"),
    ("banque, la banque, une banque", "bank"),
    ("belge, le belge, un belge", "Belgian"),
    ("belges, les belges, des belges", "Belgian"),
    ("bières, les bières, des bières", "beers"),
    ("billet d'avion, le billet d'avion, un billet d'avion", "airplane ticket"),
    ("boulangerie, la boulangerie, une boulangerie", "bakery"),
    ("bouteille, la bouteille, une bouteille", "bottle"),
    ("bouteilles, les bouteilles, des bouteilles", "bottles"),
    ("brésil, le brésil, un brésil", "Brazil"),
    ("bureau, le bureau, un bureau", "deck/office"),
    ("bus, le bus, un bus", "bus"),
    ("café, le café, un café", "coffee or café"),
    ("cafés, les cafés, des cafés", "cafés"),
    ("chaise, la chaise, une chaise", "chair"),
    ("chaises, les chaises, des chaises", "chairs"),
    ("chat, le chat, un chat", "cat"),
    ("château, le château, un château", "castle"),
    ("chats, les chats, des chats", "cats"),
    ("chaussure, la chaussure, une chaussure", "shoe"),
    ("chaussures, les chaussures, des chaussures", "shoes"),
    ("cheval, le cheval, un cheval", "horse"),
    ("chien, le chien, un chien", "dog"),
    ("chiens, les chiens, des chiens", "dogs"),
    ("chocolat, le chocolat, un chocolat", "chocolate"),
    ("chouette, la chouette, une chouette", "owl"),
    ("cinéma, le cinéma, un cinéma", "movie theater"),
    ("collègue, le or la collègue, une collègue", "colleague"),
    ("croissant, le croissant, un croissant", "croissant"),
    ("croissants, les croissants, des croissants", "croissants"),
    ("cuisine, la cuisine, une cuisine", "kitchen"),
    ("dessert, le dessert, un dessert", "dessert"),
    ("dimanche, le dimanche, un dimanche", "Sunday"),
    ("eau, l'eau, une eau", "water"),
    ("école, l'école, une école", "school"),
    ("écoles, les écoles, des écoles", "schools"),
    ("église, l'église, une église", "church"),
    ("églises, les églises, des églises", "churches"),
    ("e-mail, l'e-mail, un e-mail", "e-mail"),
    ("Espagne, l'Espagne, une Espagne", "Spain"),
    ("états-unis, les états-unis, un états-unis", "United states"),
    ("étudiant, l'étudiant, un étudiant", "student"),
    ("étudiante, l'étudiante, une étudiante", "student"),
    ("étudiantes, les étudiantes, des étudiantes", "students"),
    ("étudiants, les étudiants, des étudiants", "students"),
    ("euro, l'euro, un euro", "euro"),
    ("europe, l'europe, une europe", "europe"),
    ("euros, les euros, des euros", "euros"),
    ("famille, la famille, une famille", "family"),
    ("femme, la femme, une femme", "woman"),
    ("femmes, les femmes, des femmes", "women"),
    ("fenêtre, la fenêtre, une fenêtre", "window"),
    ("fenêtres, les fenêtres, des fenêtres", "windows"),
    ("fille, la fille, une fille", "girl"),
    ("filles, les filles, des filles", "girls"),
    ("fils, le fils, un fils", "son"),
    ("fleur, la fleur, une fleur", "flower"),
    ("fleurs, les fleurs, des fleurs", "flowers"),
    ("france, la france, une france", "france"),
    ("frère, le frère, un frère", "brother"),
    ("frères, les frères, des frères", "brothers"),
    ("garçon, le garçon, un garçon", "boy"),
    ("garçons, les garçons, des garçons", "boys"),
    ("gare, la gare, une gare", "station"),
    ("gâteau, le gâteau, un gâteau", "cake"),
    ("grand-mère, la grand-mère, une grand-mère", "grandmother"),
    ("grand-père, le grand-père, un grand-père", "grandfather"),
    ("homme, l'homme, un homme", "man"),
    ("hommes, les hommes, des hommes", "men"),
    ("hôpital, l'hôpital, un hôpital", "hospital"),
    ("hôtel, l'hôtel, un hôtel", "hotel"),
    ("immeuble, l'immeuble, un immeuble", "appartment building"),
    ("italie, l'italie, une italie", "Italy"),
    ("japon, le japon, un japon", "japan"),
    ("jardin, le jardin, un jardin", "gardin"),
    ("jeudi, le jeudi, un jeudi", "Thursday"),
    ("jour, le jour, un jour", "day"),
    ("journalist, le journalist, un journalist", "journalist"),
    ("journaliste, la journaliste, une journaliste", "journalist"),
    ("journalistes, les journalistes, des journalistes", "journalists"),
    ("jours, les jours, des jours", "days"),
    ("jupe, la jupe, une jupe", "skirt"),
    ("jupes, les jupes, des jupes", "skirts"),
    ("jus, le jus, un jus", "juice"),
    ("légume, le légume, un légume", "vegetable"),
    ("lieu, le lieu, un lieu", "place"),
    ("lit, le lit, un lit", "bed"),
    ("livre, le livre, un livre", "book"),
    ("livres, les livres, des livres", "books"),
    ("lundi, le lundi, un lundi", "Monday"),
    ("magasin, le magasin, un magasin", "store"),
    ("magasins, les magasins, des magasins", "stores"),
    ("maison, la maison, une maison", "house"),
    ("mardi, le mardi, un mardi", "Tuesday"),
    ("mari, le mari, un mari", "husband"),
    ("matin, le matin, un matin", "morning"),
    ("médecin, le médecin, un médecin", "doctor"),
    ("médecins, les médecins, des médecins", "doctors"),
    ("mercredi, le mercredi, un mercredi", "Wednesday"),
    ("mère, la mère, une mère", "mother"),
    ("message, le message, un message", "message"),
    ("messages, les messages, des messages", "messages"),
    ("métier, le métier, un métier", "job/profession"),
    ("métro, le métro, un métro", "metro"),
    ("Mexique, le Mexique, un Mexique", "Mexico"),
    ("musée, le musée, un musée", "museum"),
    ("musées, les musées, des musées", "museums"),
    ("musique, la musique, une musique", "music"),
    ("nuit, la nuit, une nuit", "night"),
    ("nuits, les nuits, des nuits", "nights"),
    ("numéro, le numéro, un numéro", "number"),
    ("œuf, l'œuf, un œuf", "egg"),
    ("orange, l'orange, une orange", "orange"),
    ("oranges, les oranges, des oranges", "oranges"),
    ("ordinateur, l'ordinateur, un ordinateur", "computer"),
    ("pantalon, les pantalon, des pantalon", "pants"),
    ("pantalons, le pantalons, un pantalons", "pants"),
    ("parc, le parc, un parc", "park"),
    ("parent, le parent, un parent", "parent"),
    ("parents, les parents, des parents", "parents"),
    ("passeport, le passeport, un passeport", "passport"),
    ("pays, le, les pays, un pays", "country"),
    ("père, le père, un père", "father"),
    ("personne, la personne, une personne", "person"),
    ("personnes, les personnes, des personnes", "people"),
    ("petit déjeuner, le petit déjeuner, un petit déjeuner", "lunch"),
    ("pharmacie, la pharmacie, une pharmacie", "pharmacy"),
    ("pharmacies, les pharmacies, des pharmacies", "pharmacies"),
    ("pièce, la pièce, une pièce", "room"),
    ("pièces, les pièces, des pièces", "rooms/coins"),
    ("pizza, la pizza, une pizza", "pizza"),
    ("pizzas, les pizzas, des pizzas", "pizzas"),
    ("plage, la plage, une plage", "beach"),
    ("plante, la plante, une plante", "plant"),
    ("plantes, les plantes, des plantes", "plants"),
    ("pont, le pont, un pont", "bridge"),
    ("ponts, les ponts, des ponts", "bridges"),
    ("porte, la porte, une porte", "door"),
    ("portes, les portes, des portes", "doors"),
    ("prix, le prix, un prix", "price or prize"),
    ("problème, le problème, un problème", "problem"),
    ("professeur, le professeur, un professeur", "professor"),
    ("professeurs, les professeurs, des professeurs", "professors"),
    ("québécois, le québécois, un québécois", "Quebecois"),
    ("québécoise, les québécoise, des québécoise", "Quebecois"),
    ("radio, la radio, une radio", "radio"),
    ("repas, le, les repas, un repas", "meal"),
    ("restaurant, le restaurant, un restaurant", "restaurant"),
    ("restaurants, les restaurants, des restaurants", "restaurants"),
    ("robe, la robe, une robe", "dress"),
    ("robes, les robes, des robes", "dresses"),
    ("route, la route, une route", "road"),
    ("routes, les routes, des routes", "roads"),
    ("rue, la rue, une rue", "street"),
    ("rues, les rues, des rues", "streets"),
    ("sac, le sac, un sac", "bag"),
    ("sacs, les sacs, des sacs", "bags"),
    ("salon, le salon, un salon", "front room"),
    ("samedi, le samedi, un samedi", "Saturday"),
    ("semaine, la semaine, une semaine", "week"),
    ("semaines, les semaines, des semaines", "weeks"),
    ("sœur, la sœur, une sœur", "sister"),
    ("sœurs, les sœurs, des sœurs", "sisters"),
    ("soir, le soir, un soir", "evening"),
    ("stations, la stations, une stations", "station"),
    ("stylo, le stylo, un stylo", "pen"),
    ("supermarché, le supermarché, un supermarché", "supermarket"),
    ("supermarchés, les supermarchés, des supermarchés", "supermarkets"),
    ("table, la table, une table", "table"),
    ("tables, les tables, des tables", "tables"),
    ("tasse, le tasse, un tasse", "cup"),
    ("tasses, les tasses, des tasses", "cups"),
    ("taxi, le taxi, un taxi", "taxi"),
    ("télé, la télé, une télé", "TV"),
    ("téléphone, le téléphone, un téléphone", "telephone"),
    ("toilettes, les toilettes, des toilettes", "toilet"),
    ("train, le train, un train", "train"),
    ("trains, les trains, des trains", "trains"),
    ("travail, le travail, un travail", "work"),
    ("t-shirt, le t-shirt, un t-shirt", "t-shirt"),
    ("université, l'université, une université", "university"),
    ("universités, les universités, des universités", "universities"),
    ("usine, l'usine, une usine", "factory"),
    ("vacances, les vacances, des vacances", "vacations"),
    ("vaches, les vaches, des vaches", "cows"),
    ("valise, la valise, une valise", "suitcase"),
    ("vélo, le vélo, un vélo", "bike"),
    ("vélos, les vélos, des vélos", "bikes"),
    ("vendredi, le vendredi, un vendredi", "Friday"),
    ("verre, le verre, un verre", "glass"),
    ("veste, la veste, une veste", "jacket"),
    ("vêtements, les vêtements, des vêtements", "clothes"),
    ("ville, la ville, une ville", "city"),
    ("visite, la visite, une visite", "visit/inspect"),
    ("voiture, la voiture, une voiture", "car"),
    ("voitures, les voitures, des voitures", "cars"),
    ("voyage, le voyage, un voyage", "journey"),
    ("zoo, le zoo, un zoo", "zoo"),
    ]
 

from gtts import gTTS
from io import BytesIO
import time

def createAudioOfString(lang, text, fileName, SpeakSlow=False):
    #fileName = fText.strip().lower().replace(" ", "_").replace("@", "'")
    tts = gTTS(text=text, lang=lang, slow=SpeakSlow)
    tts.save(fileName)
    #tts.save(".\\audio\\" + fileName + ".mp3")
    #mp3_fp = BytesIO()
    #tts.write_to_fp(mp3_fp)
    #return mp3_fp


silence1 = AudioSegment.silent(duration=3000)  # 3000 milliseconds = 3 seconds
silence2 = AudioSegment.silent(duration=1000)  # 3000 milliseconds = 3 seconds

file1 = "./nouns/tmpF.mp3"
file2 = "./nouns/tmpE.mp3"
idx = 0
for fTxt, eTxt in data:
    idx += 1
    print(idx, fTxt, eTxt)
    createAudioOfString("fr-fr",fTxt,file1,False)
    createAudioOfString("en-us",eTxt,file2,False)
    time.sleep(0.5)

    sound1 = AudioSegment.from_file(file1, format="mp3")
    sound2 = AudioSegment.from_file(file2, format="mp3")
    combined = sound1 + silence1 + sound2 + silence2 + sound1 + silence2

    # Export the combined audio as an MP3 file
    combined.export(f"./nouns/noun_{idx:03d}.mp3", format="mp3")
    



# # Load the audio files
# sound1 = AudioSegment.from_file(file1, format="mp3")
# sound2 = AudioSegment.from_file(file2, format="mp3")

# sound1.duration()


# # Create 2 seconds of silence
# silence1 = AudioSegment.silent(duration=2500)  # 3000 milliseconds = 3 seconds
# silence2 = AudioSegment.silent(duration=1000)  # 3000 milliseconds = 3 seconds

# # Combine the audio segments with silence in between
# combined = sound1 + silence1 + sound2 + silence2 + sound1

# # Export the combined audio as an MP3 file
# combined.export("combined_audio.mp3", format="mp3")


