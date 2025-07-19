// src/pages/ResultPage.jsx
import { Container, Typography, Button, Box, Breadcrumbs, Link } from '@mui/material';
import ResultDisplay from '../components/ResultDisplay';
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { 
  Home, 
  Calculate, 
  Assessment,
  Refresh
} from '@mui/icons-material';

export default function ResultPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const result = location.state?.result;

  useEffect(() => {
    if (!result) {
      console.log('❌ Pas de résultats trouvés, redirection vers accueil');
      navigate('/');
    } else {
      console.log('✅ Résultats trouvés:', result);
    }
  }, [result, navigate]);

  const handleBackToCalculator = () => {
    navigate('/calculator');
  };

  const handleBackHome = () => {
    navigate('/');
  };

  const handleNewCalculation = () => {
    navigate('/calculator', { replace: true });
  };

  if (!result) {
    return (
      <Container maxWidth="lg">
        <Typography variant="h4" align="center">
          Aucun résultat trouvé. Redirection...
        </Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      {/* Breadcrumbs Navigation */}
      <Box sx={{ mb: 3 }}>
        <Breadcrumbs aria-label="breadcrumb" sx={{ mb: 2 }}>
          <Link
            underline="hover"
            sx={{ 
              display: 'flex', 
              alignItems: 'center',
              cursor: 'pointer',
              '&:hover': { color: 'primary.main' }
            }}
            color="inherit"
            onClick={handleBackHome}
          >
            <Home sx={{ mr: 0.5 }} fontSize="inherit" />
            Accueil
          </Link>
          <Link
            underline="hover"
            sx={{ 
              display: 'flex', 
              alignItems: 'center',
              cursor: 'pointer',
              '&:hover': { color: 'primary.main' }
            }}
            color="inherit"
            onClick={handleBackToCalculator}
          >
            <Calculate sx={{ mr: 0.5 }} fontSize="inherit" />
            Calculateur
          </Link>
          <Typography
            sx={{ display: 'flex', alignItems: 'center' }}
            color="text.primary"
          >
            <Assessment sx={{ mr: 0.5 }} fontSize="inherit" />
            Résultats
          </Typography>
        </Breadcrumbs>
      </Box>

      {/* Header */}
      <Box sx={{ textAlign: 'center', mb: 4 }}>
        <Typography 
          variant="h3" 
          component="h1" 
          gutterBottom
          sx={{ 
            color: 'primary.main',
            fontWeight: 600
          }}
        >
          Résultats de votre MGP
        </Typography>
        
        <Typography variant="h6" color="text.secondary">
          Calcul pour {result.nomEtudiant}
        </Typography>
      </Box>

      {/* Résultats */}
      <ResultDisplay result={result} />

      {/* Actions */}
      <Box sx={{ 
        mt: 6, 
        display: 'flex', 
        gap: 2, 
        justifyContent: 'center',
        flexWrap: 'wrap'
      }}>
        <Button
          variant="contained"
          color="primary"
          startIcon={<Refresh />}
          onClick={handleNewCalculation}
          size="large"
        >
          Nouveau calcul
        </Button>
        
        <Button
          variant="outlined"
          color="secondary"
          startIcon={<Home />}
          onClick={handleBackHome}
        >
          Retour à l'accueil
        </Button>
      </Box>
    </Container>
  );
}