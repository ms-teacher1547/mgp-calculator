// src/services/mockApi.js - API simulée pour tests

// Fonction pour calculer la MGP
function calculateMGP(ues) {
    const totalPoints = ues.reduce((sum, ue) => sum + (ue.note * ue.credits), 0);
    const totalCredits = ues.reduce((sum, ue) => sum + ue.credits, 0);
    const mgp = totalCredits > 0 ? totalPoints / totalCredits : 0;
    
    // Déterminer la mention
    let mention = '';
    if (mgp >= 80) mention = 'Très Bien';
    else if (mgp >= 70) mention = 'Bien';
    else if (mgp >= 60) mention = 'Assez Bien';
    else if (mgp >= 50) mention = 'Passable';
    else mention = 'Échec';
    
    // Déterminer la cote pour chaque UE
    const uesWithCote = ues.map(ue => ({
      ...ue,
      cote: ue.note >= 80 ? 'A' : 
            ue.note >= 70 ? 'B' : 
            ue.note >= 60 ? 'C' : 
            ue.note >= 50 ? 'D' : 'F'
    }));
    
    return {
      id: Date.now(), // ID simulé
      nomEtudiant: '',
      mgp: mgp,
      mgpFormate: mgp.toFixed(2),
      mention: mention,
      admis: mgp >= 50,
      ues: uesWithCote,
      totalCredits: totalCredits
    };
  }
  
  // Délai simulé pour l'effet de chargement
  const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));
  
  export default {
    async calculateMGP(ues, studentName) {
      try {
        // Simuler un délai de réseau
        await delay(1000);
        
        const result = calculateMGP(ues);
        result.nomEtudiant = studentName;
        
        console.log('📊 MGP Calculé (Mode Simulation):', result);
        return result;
      } catch (error) {
        console.error('Erreur simulation:', error);
        throw new Error('Erreur lors du calcul de simulation');
      }
    },
  
    async generatePDF(id) {
      try {
        await delay(500);
        
        // Simuler un blob PDF vide
        const blob = new Blob(['PDF simulé'], { type: 'application/pdf' });
        return { data: blob };
      } catch (error) {
        console.error('Erreur PDF simulation:', error);
        throw error;
      }
    }
  };