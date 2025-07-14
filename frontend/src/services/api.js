import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/mgp',
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  async calculateMGP(ues, studentName) {
    try {
      const response = await api.post('/calculer', { 
        ues: ues.filter(ue => ue.nom && !isNaN(ue.note) && !isNaN(ue.credits)),
        nomEtudiant: studentName 
      });
      return response.data;
    } catch (error) {
      console.error('Erreur API:', {
        status: error.response?.status,
        data: error.response?.data,
        config: error.config
      });
      throw new Error(error.response?.data?.message || 'Erreur serveur');
    }
  },

  // 👉 AJOUTE CETTE FONCTION
  async generatePDF(id) {
    try {
      const response = await api.get(`/pdf/${id}`, {
        responseType: 'blob', // important pour les fichiers binaires
      });
      return response;
    } catch (error) {
      console.error('Erreur PDF:', error);
      throw error;
    }
  }
};
