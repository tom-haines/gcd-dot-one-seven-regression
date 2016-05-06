FROM java:openjdk-7-jre

# Expose the default port
EXPOSE 8000

ENV DATADIR=/opt/gcd/data
ENV DATASTORE_HOST=http://0.0.0.0:8000
ENV DATASTORE_DATASET=sample-ds

RUN mkdir -p ${DATADIR}

# Download and unpack Google Cloud Datastore
RUN curl -o gcd-v1beta3-1.0.0.zip http://storage.googleapis.com/gcd/tools/gcd-v1beta3-1.0.0.zip
RUN unzip gcd-v1beta3-1.0.0.zip && rm gcd-v1beta3-1.0.0.zip

WORKDIR /gcd

CMD ./gcd.sh create -p $DATASTORE_DATASET $DATADIR; ./gcd.sh start --host=0.0.0.0 --port=8000 $DATADIR
