// src/components/ResultDisplay.jsx
import { 
    Paper, 
    Typography, 
    Table, 
    TableBody, 
    TableCell, 
    TableContainer,
    TableRow,
    Button
  } from '@mui/material';
  import api from '../services/api';
  
  export default function ResultDisplay({ result }) {
    const handleDownload = async () => {
      try {
        const response = await api.generatePDF(result.id);
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `bulletin-mgp-${result.nomEtudiant}.pdf`);
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error('Erreur téléchargement:', error);
      }
    };
  
    if (!result) return null;
  
    return (
      <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
        <Typography variant="h5" gutterBottom>
          Résultats du Calcul
        </Typography>
  
        <Typography>
          <strong>Étudiant:</strong> {result.nomEtudiant}
        </Typography>
        <Typography>
          <strong>MGP:</strong> {result.mgpFormate} ({result.mention})
        </Typography>
        <Typography>
          <strong>Décision:</strong> {result.admis ? 'ADMIS' : 'NON ADMIS'}
        </Typography>
  
        <TableContainer component={Paper} sx={{ mt: 2, mb: 2 }}>
          <Table>
            <TableBody>
              {result.ues.map((ue, index) => (
                <TableRow key={index}>
                  <TableCell>{ue.nom}</TableCell>
                  <TableCell>{ue.note}/100</TableCell>
                  <TableCell>{ue.cote}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
  
        <Button 
          onClick={handleDownload}
          variant="contained" 
          color="success"
        >
          Télécharger le Bulletin (PDF)
        </Button>
      </Paper>
    );
  }