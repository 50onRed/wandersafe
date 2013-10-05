import csv
import json
import requests

def main(filename):
    with open(filename) as f:
        crime_data = []
        reader = csv.reader(f)

        for line in reader:
            date = line[3]
            block = line[7]
            code = line[8]
            crime = line[10]
            lon = line[12]
            lat = line[13]

            # if there aren't any coordinates, don't even bother loading into the stackmob schema
            # since we will never query this record
            if not (lat and lon):
                continue

            headers = {
                    'X-StackMob-API-Key': '9f08750f-14b1-4e37-9867-9c54d7f4f0d5',
                    'Content-Type': 'application/json',
                    'Accept': 'application/vnd.stackmob+json; version=0'
                    }

            crime_data.append({
                    'dispatch_date': date,
                    'location_block': block,
                    'text_general_code': crime,
                    'ucr_general': int(code),
                    'location': {
                        'lat': float(lat),
                        'lon': float(lon)
                        }
                    })

        response = requests.post('http://api.stackmob.com/crime', headers=headers,
                data=json.dumps(crime_data))

        print response.text

if __name__ == '__main__':
    import sys
    main(sys.argv[1])
