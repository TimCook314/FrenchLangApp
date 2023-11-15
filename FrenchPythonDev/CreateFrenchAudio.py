import sys
import os
from gtts import gTTS

def createFrenchAudioOfString(frenchText, fileName, T_F):
    SpeakSlow = False
    if (T_F[0:1] == "T"):
        SpeakSlow = True
    #fileName = fText.strip().lower().replace(" ", "_").replace("@", "'")
    fObj = gTTS(text=frenchText, lang="fr-fr", slow=SpeakSlow)
    fObj.save(".\\audio\\" + fileName + ".mp3")


if __name__ == "__main__":
    if len(sys.argv) < 4:
        print('Usage: python idk.py "your string with spaces", "file name", T/F for slow)')
        sys.exit(1)
    #print("Arguments: " + str(len(sys.argv)))
    input_string = sys.argv[1]
    fileName = sys.argv[2]
    T_F = sys.argv[3]
    createFrenchAudioOfString(input_string,fileName,T_F)
    print("OK")
    sys.exit(0)
    
