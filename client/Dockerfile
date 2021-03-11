FROM node:12.18.4

WORKDIR /app

ENV PATH /app/node_modules.bin:$PATH

# install app dependecies
COPY package.json ./
COPY package-lock.json ./

RUN npm install react-scripts
RUN npm install

COPY . ./

# start app
CMD ["npm", "start"]
