FROM python:3.11-slim

WORKDIR /app

COPY app.py .
COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 8082

CMD ["python", "app.py", "8082"]
