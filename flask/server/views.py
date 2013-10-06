from pprint import pprint
from flask import request, render_template
from .stackmob import get_crime_near
import json

def index():
    return render_template("index.html")

def tell_me_if_im_going_to_die(lat, lon, meters):
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    level = process_crime_level(response)
    return str(level)

def get_crimes(lat, lon, meters):
    lat, lon, meters = float(lat), float(lon), float(meters)
    l = get_crime_near(lat, lon, meters)
    return json.dumps(l)

def show_map(lat, lon):
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    return render_template("map.html", lat=lat, lon=lon, crimes=response)

def process_crime_level(crimes):
    pprint(crimes)
    if len(crimes) == 0:
        return 0 # avoid division by zero
    sum_of_distances = sum([float(crime['location']['distance']) for crime in crimes])
    total = sum([weight_crime_level(crime, sum_of_distances) for crime in crimes])
    new_sum_of_distances = sum([sum_of_distances - float(crime['location']['distance']) for crime in crimes])
    return 8 - int(total / 100.0 / new_sum_of_distances)

def weight_crime_level(crime, sum_of_distances):
    distance = float(crime['location']['distance'])
    return float(crime['ucr_general']) * (sum_of_distances - distance)
