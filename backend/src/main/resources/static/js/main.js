function generateContent() {
    const content = document.getElementById('content').value;
    fetch('/generate-content?content=' + content)
        .then(response => response.text())
        .then(content => document.getElementById('generated-content').textContent = content)
        .catch(error => console.error('Error generating content:', error));
}