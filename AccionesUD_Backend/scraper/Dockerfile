# Imagen base
FROM python:3.11-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos necesarios
COPY . /app

# Instala dependencias
RUN pip install --upgrade pip && \
    pip install -r requirements.txt

# Instala los navegadores necesarios para Playwright
RUN apt-get update && apt-get install -y wget gnupg curl ca-certificates && \
    apt-get install -y libnss3 libatk1.0-0 libatk-bridge2.0-0 libxcomposite1 libxrandr2 libxdamage1 libxext6 libxfixes3 libx11-xcb1 libxss1 libasound2 libgbm1 libxshmfence1 libglib2.0-0 libgtk-3-0 libdrm2 && \
    playwright install --with-deps

# Expone el puerto del microservicio
EXPOSE 8000

# Comando para iniciar el servidor FastAPI
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
