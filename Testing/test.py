import requests
from bs4 import BeautifulSoup


# all school ids on ratemyprofessor
school_ids = []

# get all school ids
url = 'https://www.ratemyprofessors.com/search.jsp?queryBy=schoolId&schoolName=university+of+california+berkeley&schoolID=1075&queryoption=TEACHER'