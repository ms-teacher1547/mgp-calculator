// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/mgp',
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  calculateMGP(ues, studentName) {
    return api.post('/calculer', { 
      ues,
      nomEtudiant: studentName 
    });
  },
  
  generatePDF(resultId) {
    return api.get(`/telecharger-pdf/${resultId}`, {
      responseType: 'blob'
    });
  }
};