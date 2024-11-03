import csv
from sqlalchemy import create_engine, Column, Integer, String, Enum, ForeignKey, Float
#from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import inspect
import random

# Define the database URL
DATABASE_URL = "sqlite:///nouns.db"

# Create the database engine
engine = create_engine(DATABASE_URL, echo=True)

# Define the base class for the models
Base = declarative_base()
#Base = sqlalchemy.orm.declarative_base()

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

# Verify that the table matches the class definition
def verify_and_update_table(Base, engine, model):
    table_name = model.__tablename__
    inspector = inspect(engine)
    if not inspector.has_table(table_name):
        Base.metadata.tables[table_name].create(engine)
    else:
        columns_in_db = {col['name'] for col in inspector.get_columns(table_name)}
        columns_in_model = {col.name for col in model.__table__.columns}
        # Columns to add
        columns_to_add = columns_in_model - columns_in_db
        # Columns to remove
        columns_to_remove = columns_in_db - columns_in_model
        with engine.connect() as connection:
            if columns_to_add:
                for column in columns_to_add:
                    col_type = str(model.__table__.columns[column].type)
                    from sqlalchemy import text
                    connection.execute(text(f'ALTER TABLE {table_name} ADD COLUMN {column} {col_type}'))
            if columns_to_remove:
                    # SQLite does not support dropping columns directly, so we need to recreate the table
                    temp_table_name = f"{table_name}_temp"
                    columns_to_keep = columns_in_model - columns_to_remove
                    columns_to_keep_str = ", ".join(columns_to_keep)
                    connection.execute(text(f'CREATE TABLE {temp_table_name} AS SELECT {columns_to_keep_str} FROM {table_name}'))
                    connection.execute(text(f'DROP TABLE {table_name}'))
                    connection.execute(text(f'ALTER TABLE {temp_table_name} RENAME TO {table_name}'))





# Verify and update the tables
verify_and_update_table(Base, engine, Noun)
verify_and_update_table(Base, engine, Exercise_1)


# Create a session
Session = sessionmaker(bind=engine)
session = Session()

# Read data from the CSV file and insert it into the database
with open('nouns.csv', newline='', encoding='utf-8') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        # Check if the fText already exists in the database
        existing_noun = session.query(Noun).filter_by(fText=row['fText']).first()
        if not existing_noun:
            noun = Noun(
                eText=row['eText'],
                article=row['article'],
                fText=row['fText'],
                gender=row['gender'],
                quantity=row['quantity']
            )
            session.add(noun)

# Commit the session to save the data
session.commit()
# Close the session
session.close()