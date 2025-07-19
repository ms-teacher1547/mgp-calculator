// src/pages/CalculatorPage.jsx
import { Container, Typography, Box, Button, Breadcrumbs, Link } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { Home, Calculate, ArrowBack } from '@mui/icons-material';
import UeInputForm from '../components/UeInputForm';

export default function CalculatorPage() {
  const navigate = useNavigate();

  const handleBackHome = () => {
    navigate('/');
  };

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
          <Typography
            sx={{ display: 'flex', alignItems: 'center' }}
            color="text.primary"
          >
            <Calculate sx={{ mr: 0.5 }} fontSize="inherit" />
            Calculateur MGP
          </Typography>
        </Breadcrumbs>

        <Button
          onClick={handleBackHome}
          startIcon={<ArrowBack />}
          variant="outlined"
          size="small"
          sx={{ mb: 2 }}
        >
          Retour à l'accueil
        </Button>
      </Box>

      {/* Header Section */}
      <Box sx={{ textAlign: 'center', mb: 4 }}>
        <Typography 
          variant="h3" 
          component="h1" 
          gutterBottom
          sx={{ 
            color: 'primary.main',
            fontWeight: 600,
            mb: 2
          }}
        >
          Calculateur de MGP
        </Typography>
        
        <Typography 
          variant="h6" 
          color="text.secondary" 
          sx={{ mb: 3, maxWidth: 600, mx: 'auto' }}
        >
          Saisissez vos notes et crédits pour calculer automatiquement votre Moyenne Générale Pondérée
        </Typography>

        {/* Instructions rapides */}
        <Box 
          sx={{ 
            display: 'flex', 
            justifyContent: 'center',
            flexWrap: 'wrap',
            gap: 2,
            mb: 4
          }}
        >
          <Box sx={{ 
            display: 'flex', 
            alignItems: 'center',
            backgroundColor: 'info.50',
            px: 2,
            py: 1,
            borderRadius: 2,
            border: '1px solid',
            borderColor: 'info.200'
          }}>
            <Typography variant="body2" color="info.main">
              📝 Remplissez vos UE
            </Typography>
          </Box>
          
          <Box sx={{ 
            display: 'flex', 
            alignItems: 'center',
            backgroundColor: 'warning.50',
            px: 2,
            py: 1,
            borderRadius: 2,
            border: '1px solid',
            borderColor: 'warning.200'
          }}>
            <Typography variant="body2" color="warning.main">
              ⚡ Aperçu en temps réel
            </Typography>
          </Box>
          
          <Box sx={{ 
            display: 'flex', 
            alignItems: 'center',
            backgroundColor: 'success.50',
            px: 2,
            py: 1,
            borderRadius: 2,
            border: '1px solid',
            borderColor: 'success.200'
          }}>
            <Typography variant="body2" color="success.main">
              🎯 Résultats détaillés
            </Typography>
          </Box>
        </Box>
      </Box>

      {/* Formulaire Principal */}
      <UeInputForm />
    </Container>
  );
}