

import csv
from sqlalchemy import create_engine, Column, Integer, String, Enum, ForeignKey, Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy import inspect, func
import random
import msvcrt

# Define the database URL
DATABASE_URL = "sqlite:///nouns.db"

# Create the database engine
#engine = create_engine(DATABASE_URL, echo=True)
engine = create_engine(DATABASE_URL, echo=True)

# Define the base class for the models
Base = declarative_base()

# Define the Noun model
class Noun(Base):
    __tablename__ = 'nouns'
    id = Column(Integer, primary_key=True, autoincrement=True)
    eText = Column(String, nullable=False)
    article = Column(String, nullable=False)
    fText = Column(String, nullable=False)
    gender = Column(Enum('m', 'f'), nullable=False)
    quantity = Column(Enum('s', 'p'), nullable=False)

# Define the Exercise_1 model
class Exercise_1(Base):
    __tablename__ = 'exercises_1'
    id = Column(Integer, primary_key=True, autoincrement=True)
    noun_id = Column(Integer, ForeignKey('nouns.id'))
    performance = Column(Float, default=0.0)
    attempts = Column(Integer, default=0)
    importance = Column(Float, default=1.0)

# Create a session
Session = sessionmaker(bind=engine)
session = Session()

#Do a session

# Create an entry for each noun in the Exercise_1 table
nouns = session.query(Noun).all()

for noun in nouns:
    existing_exercise = session.query(Exercise_1).filter_by(noun_id=noun.id).first()
    if not existing_exercise:
        print(f"not there {noun.fText}")
        exercise = Exercise_1(noun_id=noun.id)
        session.add(exercise)
# Commit the changes
session.commit()




# Calculate the average number of attempts
ave_attempts = max(1, session.query(func.avg(Exercise_1.attempts)).scalar())

# Get all exercise entries
exercises = session.query(Exercise_1).all()

# For each exercise, print the noun, performance, and importance
cards = []
for exercise in exercises:
    noun = session.query(Noun).filter_by(id=exercise.noun_id).first()
    if noun:
        # Calculate the session_weight  = performance * importance
        session_weight = exercise.performance * random.uniform(0, 1) * max(0.3, min(1.0, exercise.attempts / ave_attempts)) * exercise.importance
        print(f"Noun: {noun.eText}, Performance: {exercise.performance}, Importance: {exercise.importance}, session_weight: {session_weight}")
        cards.append({
            'noun.id': noun.id,
            'eText': noun.eText,
            'fText': noun.fText,
            'article': noun.article,
            'quantity': noun.quantity,
            'exercise.id': exercise.id,
            'session_weight': session_weight,
            'session_score': 0.0,
            'session_attempts': 0
        })

# Sort the cards by session_weight
cards = sorted(cards, key=lambda x: x['session_weight'], reverse=True)

# Print the cards
for card in cards:
    print(card)

while cards:
    card_set = []
    if len(cards) <= 4:
        card_set = cards
        cards = []
    else:
        #= cards[:2] + cards[-2:]
        card_set.append( cards.pop(0) )
        card_set.append( cards.pop(-1) )
        card_set.append( cards.pop(0) )
        card_set.append( cards.pop(-1) )

    print("\n\nPress Enter to see the result after the card")
    for card in card_set:
        #print(card)
        print(f"Card: {card['fText']}", end="")
        input()
        print(f"  -> {card['eText']}")
        print("Enter scrore 0/1")
        char = msvcrt.getch()
        print(f"You pressed: {char.decode('utf-8')}")
        if char not in [b'0', b'1']:
            print("Invalid input. Please enter '0' or '1'.")
            continue
        score = float(char.decode('utf-8'))
        print(f"Converted score: {score}")
        # Increment the exercise attempts for the first card
        exercise = session.query(Exercise_1).filter_by(id=card['exercise.id']).first()
        exercise.attempts += 1
        # Update the performance for the first card
        #exercise.performance = (exercise.performance * (exercise.attempts - 1) + score) / exercise.attempts
        a_factor = 0.2
        exercise.performance = (1.0 - a_factor) * exercise.performance + a_factor * score
        #
        card['session_attempts'] += 1
        card['session_score'] += (2*score-1) / min(2,card['session_attempts'])
        #What to do with card
        if card['session_score'] < 0.3:
            #Put back in deck
            cards.insert(0, card)
        elif card['session_score'] < 0.8:
            #Put back in deck at random position
            cards.insert(random.randint(0,len(cards)), card)
        #     pass
        #     #exercise.importance = max(0.1, exercise.importance - 0.1)
        # else:
        #     exercise.importance = min(1.0, exercise.importance + 0.1)

    # Commit the changes
    session.commit()






# Commit the changes
session.commit()
# Close the session
session.close()
