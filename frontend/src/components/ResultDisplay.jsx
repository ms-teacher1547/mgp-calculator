// src/components/ResultDisplay.jsx
import { 
  Paper, 
  Typography, 
  Table, 
  TableBody, 
  TableCell, 
  TableContainer,
  TableRow,
  TableHead,
  Button,
  Box,
  Card,
  CardContent,
  Chip,
  Divider
} from '@mui/material';
import {
  CheckCircle,
  Cancel,
  School,
  Grade,
  Assessment,
  Download
} from '@mui/icons-material';
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
      alert('Fonctionnalité PDF temporairement indisponible');
    }
  };

  if (!result) {
    return (
      <Card>
        <CardContent>
          <Typography variant="h6" color="error">
            Aucun résultat à afficher
          </Typography>
        </CardContent>
      </Card>
    );
  }

  const getResultColor = (admis) => admis ? 'success' : 'error';
  const getResultIcon = (admis) => admis ? <CheckCircle /> : <Cancel />;

  return (
    <Box>
      {/* Résumé Principal */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
            <School sx={{ mr: 2, color: 'primary.main' }} />
            <Typography variant="h5">
              Résultats du Calcul MGP
            </Typography>
          </Box>

          <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(3, 1fr)' }, gap: 3 }}>
            {/* MGP */}
            <Box sx={{ textAlign: 'center' }}>
              <Typography variant="h3" color="primary.main" gutterBottom>
                {result.mgpFormate}
              </Typography>
              <Typography variant="h6" color="text.secondary">
                MGP /100
              </Typography>
            </Box>

            {/* Mention */}
            <Box sx={{ textAlign: 'center' }}>
              <Chip 
                label={result.mention}
                color={result.mgp >= 70 ? 'success' : result.mgp >= 60 ? 'warning' : 'error'}
                size="large"
                sx={{ fontSize: '1rem', py: 2 }}
              />
              <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                Mention
              </Typography>
            </Box>

            {/* Décision */}
            <Box sx={{ textAlign: 'center' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', mb: 1 }}>
                {getResultIcon(result.admis)}
                <Typography 
                  variant="h6" 
                  color={getResultColor(result.admis)}
                  sx={{ ml: 1 }}
                >
                  {result.admis ? 'ADMIS' : 'NON ADMIS'}
                </Typography>
              </Box>
              <Typography variant="body2" color="text.secondary">
                Décision
              </Typography>
            </Box>
          </Box>
        </CardContent>
      </Card>

      {/* Détails par UE */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <Assessment sx={{ mr: 2, color: 'primary.main' }} />
            <Typography variant="h6">
              Détail par Unité d'Enseignement
            </Typography>
          </Box>

          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: 600 }}>UE</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Note /100</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Crédits</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Cote</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {result.ues.map((ue, index) => (
                  <TableRow key={index} sx={{ '&:hover': { backgroundColor: 'action.hover' } }}>
                    <TableCell>
                      <Typography variant="body1" fontWeight={500}>
                        {ue.nom}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography 
                        variant="body1"
                        color={ue.note >= 50 ? 'success.main' : 'error.main'}
                        fontWeight={500}
                      >
                        {ue.note}
                      </Typography>
                    </TableCell>
                    <TableCell>{ue.credits || 6}</TableCell>
                    <TableCell>
                      <Chip 
                        label={ue.cote}
                        size="small"
                        color={
                          ue.cote === 'A' || ue.cote === 'B' ? 'success' :
                          ue.cote === 'C' ? 'warning' : 'error'
                        }
                      />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          <Divider sx={{ my: 2 }} />
          
          <Typography variant="body2" color="text.secondary">
            <strong>Total crédits:</strong> {result.totalCredits || result.ues.reduce((sum, ue) => sum + (ue.credits || 6), 0)}
          </Typography>
        </CardContent>
      </Card>

      {/* Bouton PDF */}
      <Card>
        <CardContent sx={{ textAlign: 'center' }}>
          <Button 
            onClick={handleDownload}
            variant="contained" 
            color="success"
            startIcon={<Download />}
            size="large"
          >
            Télécharger le Bulletin (PDF)
          </Button>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Format officiel pour vos démarches
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
}