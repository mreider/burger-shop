import time
import requests
import mysql.connector
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.requests import RequestsInstrumentor
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
import logging
import random
import json
import os

# Database connection
def truncate_orders():
    try:
        connection = mysql.connector.connect(
            host="mysql",
            user="root",
            password="root",
            database="burger_shop"
        )
        cursor = connection.cursor()
        cursor.execute("TRUNCATE TABLE orders")
        cursor.execute("ALTER TABLE orders AUTO_INCREMENT = 1")
        connection.commit()
        cursor.close()
        connection.close()
        print("Orders table truncated and primary key reset.")
    except mysql.connector.Error as err:
        print(f"Error: {err}")

merged = dict()
for name in ["dt_metadata_e617c525669e072eebe3d0f08212e8f2.json", "/var/lib/dynatrace/enrichment/dt_metadata.json", "/var/lib/dynatrace/enrichment/dt_host_metadata.json"]:
    try:
        data = ''
        with open(name) as f:
            data = json.load(f if name.startswith("/var") else open(f.read()))
            merged.update(data)
    except:
        pass
merged["KUBERNETES_CONTAINER_NAME"] = "fulfullmentservice"
endpoint = "https://abl46885.dev.dynatracelabs.com/api/v2/otlp/v1/traces"
resource = Resource.create(merged)
token_string = "Api-Token " + os.getenv('DYNATRACE_TOKEN')
format = "%(asctime)s %(levelname)s [%(name)s] [%(filename)s:%(lineno)d] - %(message)s"
tracer_provider = TracerProvider(resource=resource)
span_processor = BatchSpanProcessor(OTLPSpanExporter(endpoint=endpoint, headers={"Authorization": token_string}))
tracer_provider.add_span_processor(span_processor)
RequestsInstrumentor().instrument()
trace.set_tracer_provider(tracer_provider)
tracer = trace.get_tracer(__name__)
logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s [%(name)s] [%(filename)s:%(lineno)d] - %(message)s")
logger = logging.getLogger(__name__)

def do_constantly():
    with tracer.start_as_current_span("post-order"):
        headers = {'Accept': 'text/plain'}
        data = {'quantity': random.randint(1, 4)}  # Generate a random number between 1 and 4
        resp = requests.post(url="http://frontendservice/submitOrder", headers=headers, data=data)
        logger.info(f"Status Code: {resp.status_code}, Response: {resp.text}")

# Truncate orders every few hours
def periodic_truncate(interval_hours):
    while True:
        truncate_orders()
        time.sleep(interval_hours * 3600)

# Start the truncation in a separate thread
import threading
truncate_thread = threading.Thread(target=periodic_truncate, args=(4,))  # Adjust the interval as needed
truncate_thread.start()

while True:
    do_constantly()
    frequency = float(os.getenv('FREQUENCY', 1))
    seconds = random.uniform(frequency, 11)
    time.sleep(seconds)
