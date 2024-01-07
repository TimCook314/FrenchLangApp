

from pydub import AudioSegment


file1 = "audio\\samedi.T.mp3"
file2 = "audio\\mardi.T.mp3"


# Load the audio files
sound1 = AudioSegment.from_file(file1, format="mp3")
sound2 = AudioSegment.from_file(file2, format="mp3")

# Create 3 seconds of silence
silence = AudioSegment.silent(duration=3000)  # 3000 milliseconds = 3 seconds

# Combine the audio segments with silence in between
combined = sound1 + silence + sound2

# Export the combined audio as an MP3 file
combined.export("combined_audio.mp3", format="mp3")


