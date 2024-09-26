# Use the official Nginx image
FROM nginx:alpine

# Copy static files to Nginx's html directory
COPY ./static /usr/share/nginx/html
#COPY ./patient /usr/share/nginx/html/patient

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx.conf

# Expose port 80 to make the service accessible
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
