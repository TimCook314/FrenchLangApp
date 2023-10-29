import sys
import os
from gtts import gTTS


def createFrenchAudioOfString(frenchText, fileName):
    #fileName = fText.strip().lower().replace(" ", "_").replace("@", "'")
    fObj = gTTS(text=frenchText, lang="fr-fr", slow=True)
    fObj.save(".\\audio\\" + fileName + ".mp3")


def count_letters_in_string(input_string):
    letter_count = sum(1 for char in input_string if char.isalpha())
    return letter_count


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python count_letters.py 'your string with spaces'")
        sys.exit(1)

    #print("Arguments: " + str(len(sys.argv)))
    input_string = sys.argv[1]
    fileName = sys.argv[2]
    createFrenchAudioOfString(input_string,fileName)
    print("OK")
    sys.exit(0)
    
    #letter_count = count_letters_in_string(input_string)
    #print("Letter count:", letter_count)
