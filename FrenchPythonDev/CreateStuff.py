print("Hello")

import os
from gtts import gTTS
from io import BytesIO

fText = "Je suis grand!"
eText = "Hello"

fObj = gTTS(text=fText, lang="fr-fr", slow=False)
eObj = gTTS(text=eText, lang=   "en", slow=False)

mp3_fp = BytesIO()
fObj.write_to_fp(mp3_fp)

#from pydub import AudioSegment
#from pydub.playback import play
#import io
#data = open('filename.mp3', 'rb').read()
#song = AudioSegment.from_file(io.BytesIO(data), format="mp3")
#play(song)




fObj.save("bon.mp3")
eObj.save("hello.mp3")



