
# # Create the tables if they do not exist
# # inspector = inspect(engine)
# # if not inspector.has_table('nouns'):
# #     Base.metadata.tables['nouns'].create(engine)
# # if not inspector.has_table('exercises_1'):
# #     Base.metadata.tables['exercises_1'].create(engine)

# # exit()

# # Create a session
# Session = sessionmaker(bind=engine)
# session = Session()

# # Read data from the CSV file and insert it into the database
# with open('nouns.csv', newline='', encoding='utf-8') as csvfile:
#     reader = csv.DictReader(csvfile)
#     for row in reader:
#         # Check if the fText already exists in the database
#         existing_noun = session.query(Noun).filter_by(fText=row['fText']).first()
#         if not existing_noun:
#             noun = Noun(
#                 eText=row['eText'],
#                 article=row['article'],
#                 fText=row['fText'],
#                 gender=row['gender'],
#                 quantity=row['quantity']
#             )
#             session.add(noun)

# # Commit the session to save the data
# session.commit()


# # # Create an entry for each noun in the Exercise_1 table
# # nouns = session.query(Noun).all()
# # for noun in nouns:
# #     existing_exercise = session.query(Exercise_1).filter_by(noun_id=noun.id).first()
# #     if not existing_exercise:
# #         exercise = Exercise_1(noun_id=noun.id)
# #         session.add(exercise)
# # Commit the session to save the data
# # session.commit()



# # from collections import deque

# # class RunningAverage:
# #     def __init__(self, size):
# #         self.size = size
# #         self.queue = deque(maxlen=size)
# #         self.total = 0

# #     def add_value(self, value):
# #         if len(self.queue) == self.size:
# #             self.total -= self.queue[0]
# #         self.queue.append(value)
# #         self.total += value

# #     def get_average(self):
# #         if not self.queue:
# #             return 0
# #         return self.total / len(self.queue)

# # # I would like a function that gets a random entry for the exercises_1 table and returns the eText, article, and fText
# # def show_random_exercise_1():

# #     # Create an entry for each noun in the Exercise_1 table
# #     nouns = session.query(Noun).all()
# #     for noun in nouns:
# #         existing_exercise = session.query(Exercise_1).filter_by(noun_id=noun.id).first()
# #         if not existing_exercise:
# #             exercise = Exercise_1(noun_id=noun.id)
# #             session.add(exercise)

    


# #     # Get all exercise entries
# #     exercises = session.query(Exercise_1).all()
# #     if not exercises:
# #         return None
    
# #     keep_going = True
# #     while keep_going:
# #         # Select a random exercise
# #         random_exercise = random.choice(exercises)
# #         # Get the corresponding noun
# #         noun = session.query(Noun).filter_by(id=random_exercise.noun_id).first()
# #         running
# #          noun.id
# #         if noun:
# #             # print the noun
# #             print("")
# #             print(noun.fText)
# #             txt = input("Press Enter to continue...")
# #             if txt == "q":
# #                 keep_going = False
# #                 break
# #             print(f"article {noun.article}, eText {noun.eText}")
# #             txt = input("Enter 0 or 1 to continue...")
# #             if txt == "q":
# #                 keep_going = False
# #                 break
            


# #             return {
# #                 'eText': noun.eText,
# #                 'article': noun.article,
# #                 'fText': noun.fText
# #             }
# #     # Select a random exercise
# #     random_exercise = random.choice(exercises)
# #     # Get the corresponding noun
# #     noun = session.query(Noun).filter_by(id=random_exercise.noun_id).first()
# #     if noun:
# #         return {
# #             'eText': noun.eText,
# #             'article': noun.article,
# #             'fText': noun.fText
# #         }
# #     return None



# # # Close the session
# # session.close()