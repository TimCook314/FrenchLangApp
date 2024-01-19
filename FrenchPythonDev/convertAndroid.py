import os
import time
import shutil
from io import BytesIO
from tkinter import filedialog
from pydub import AudioSegment

print("hi")

indir = r"C:\TimCook\Franch3Audio\Inputs"
otdir = r"C:\TimCook\Franch3Audio\Outputs"

srcDirectory = filedialog.askdirectory(initialdir=indir)
myList = os.listdir(srcDirectory)

print(srcDirectory)
head, tail = os.path.split(srcDirectory)
dstDirectory = os.path.join(otdir, tail)

if os.path.isdir(dstDirectory):
    shutil.rmtree(dstDirectory)
os.makedirs(dstDirectory)

for fName in myList:
    if fName.endswith("fr.wav"):
        print(fName)
        file1 = os.path.join(srcDirectory, fName)
        file2 = os.path.join(srcDirectory, fName.removesuffix("fr.wav") + "en.wav")
        file3 = os.path.join(dstDirectory, fName.removesuffix("fr.wav") + ".mp3")
        sound1 = AudioSegment.from_file(file1, format="wav")
        sound2 = AudioSegment.from_file(file2, format="wav")

        silence1_duration = max(sound1.duration_seconds * 1.5, 2.0)
        silence2_duration = max(sound2.duration_seconds * 1.0, 2.0)
        silence3_duration = max(sound1.duration_seconds * 1.0, 1.5)

        silence1 = AudioSegment.silent(duration=int(silence1_duration * 1000))
        silence2 = AudioSegment.silent(duration=int(silence2_duration * 1000))
        silence3 = AudioSegment.silent(duration=int(silence3_duration * 1000))

        combined = sound1 + silence1 + sound2 + silence2 + sound1 + silence2

        combined.export(file3, format="mp3")




