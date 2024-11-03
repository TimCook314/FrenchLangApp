

# Open the noun database

# Need to work on the keeping track of

from collections import deque

class RunningAverage:
    def __init__(self, size):
        self.size = size
        self.queue = deque(maxlen=size)
        self.total = 0

    def add_value(self, value):
        if len(self.queue) == self.size:
            self.total -= self.queue[0]
        self.queue.append(value)
        self.total += value

    def get_average(self):
        if not self.queue:
            return 0
        return self.total / len(self.queue)
    
# Define a table for exercises_1
# Title: "French word for..."
# In this excerise, the user is given an English word and must provide the French translation."


# the table should have the following columns:
# id (primary key)
# a reference to what noun is being tested from the noun table
# the number of times the user has attempted the exercise
# the number of times the user has answered correctly
# the running average for each of the last 5 sessions
# the importance of the noun (1-5)

class Exercise_1(Base):
    __tablename__ = 'exercises_1'
    id = Column(Integer, primary_key=True, autoincrement=True)
    noun_id = Column(Integer, ForeignKey('nouns.id'))
    attempts = Column(Integer, default=0)
    correct = Column(Integer, default=0)



    # session_running_avg = Column(Float, default=0)
    # running_avg_5 = Column(Float, default=0)
    # importance = Column(Integer, default=3)





# class Exercise_1(Base):
#     __tablename__ = 'exercises_1'
#     id = Column(Integer, primary_key=True, autoincrement=True)
#     noun_id = Column(Integer, ForeignKey('nouns.id'))
#     attempts = Column(Integer, default=0)
#     correct = Column(Integer, default=0)
#     session_running_avg = Column(Float, default=0)
#     running_avg_10 = Column(Float, default=0)
#     importance = Column(Integer, default=3)



#Go get 






# the number of times the user has answered incorrectly
# the last time the user attempted the exercise
# the last time the user answered correctly
# the last time the user answered incorrectly
# the next time the user should attempt the exercise
# the next time the user should answer correctly



# Define a table for exercises_2
# Title: "English word for..."
# In this excerise, the user is given a French word and must provide the English translation."

# Define a table for exercises_3
# Title: "French noun audio..."
# In this excerise, the user is given a French word audio and must provide the English translation."
