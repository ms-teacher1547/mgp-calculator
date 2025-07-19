// src/pages/HomePage.jsx
import { Container, Typography, Box, Card, CardContent, Button, CardActions } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { 
  Calculate, 
  TrendingUp, 
  School, 
  Assessment,
  ArrowForward,
  GetApp
} from '@mui/icons-material';

function FeatureCard({ icon, title, description, actionText, actionColor = "primary", onClick, disabled = false }) {
  return (
    <Card 
      sx={{ 
        height: '100%', 
        display: 'flex',
        flexDirection: 'column',
        transition: 'all 0.3s ease',
        '&:hover': { 
          transform: disabled ? 'none' : 'translateY(-8px)',
          boxShadow: disabled ? 2 : 6
        },
        opacity: disabled ? 0.6 : 1,
        cursor: disabled ? 'not-allowed' : 'pointer'
      }}
      onClick={disabled ? undefined : onClick}
    >
      <CardContent sx={{ textAlign: 'center', py: 3, flexGrow: 1 }}>
        <Box sx={{ color: disabled ? 'text.secondary' : 'primary.main', mb: 2 }}>
          {icon}
        </Box>
        <Typography variant="h6" gutterBottom>
          {title}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          {description}
        </Typography>
      </CardContent>
      <CardActions sx={{ justifyContent: 'center', pb: 2 }}>
        <Button
          variant="contained"
          color={actionColor}
          endIcon={<ArrowForward />}
          disabled={disabled}
          sx={{ borderRadius: 20 }}
        >
          {actionText}
        </Button>
      </CardActions>
    </Card>
  );
}

export default function HomePage() {
  const navigate = useNavigate();

  const handleCalculateClick = () => {
    navigate('/calculator');
  };

  const handleVisualizationClick = () => {
    // Futur : navigate('/analytics');
    alert('Fonctionnalité bientôt disponible !');
  };

  const handleAnalysisClick = () => {
    // Futur : navigate('/reports');
    alert('Fonctionnalité bientôt disponible !');
  };

  const handleExportClick = () => {
    alert('Cette fonctionnalité est disponible après le calcul de votre MGP !');
  };

  return (
    <Container maxWidth="lg">
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 8, py: 4 }}>
        <Typography 
          variant="h1" 
          component="h1" 
          gutterBottom
          sx={{ 
            background: 'linear-gradient(45deg, #1e3a8a, #3b82f6)',
            backgroundClip: 'text',
            WebkitBackgroundClip: 'text',
            color: 'transparent',
            mb: 2,
            fontSize: { xs: '2rem', md: '2.5rem' }
          }}
        >
          Calculateur MGP
        </Typography>
        <Typography variant="h5" color="text.secondary" gutterBottom>
          Université de Yaoundé 1
        </Typography>
        <Typography 
          variant="body1" 
          color="text.secondary" 
          sx={{ maxWidth: 600, mx: 'auto', mt: 3, mb: 4 }}
        >
          Calculez facilement votre Moyenne Générale Pondérée et visualisez vos résultats académiques
          avec notre outil moderne et intuitif.
        </Typography>

        {/* CTA Principal */}
        <Button
          variant="contained"
          size="large"
          onClick={handleCalculateClick}
          startIcon={<Calculate />}
          sx={{ 
            py: 2, 
            px: 4, 
            fontSize: '1.1rem',
            borderRadius: 3,
            boxShadow: 3,
            '&:hover': {
              boxShadow: 6,
              transform: 'translateY(-2px)'
            }
          }}
        >
          Commencer le Calcul
        </Button>
      </Box>

      {/* Features Grid */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" align="center" gutterBottom sx={{ mb: 4 }}>
          Fonctionnalités Disponibles
        </Typography>
        
        <Box 
          sx={{ 
            display: 'grid',
            gridTemplateColumns: {
              xs: '1fr',
              sm: 'repeat(2, 1fr)',
              md: 'repeat(4, 1fr)'
            },
            gap: 4
          }}
        >
          <FeatureCard
            icon={<Calculate sx={{ fontSize: 48 }} />}
            title="Calcul Automatique"
            description="Calculez votre MGP en quelques clics selon le système officiel UY1"
            actionText="Calculer"
            actionColor="primary"
            onClick={handleCalculateClick}
          />
          
          <FeatureCard
            icon={<TrendingUp sx={{ fontSize: 48 }} />}
            title="Visualisation"
            description="Graphiques interactifs et statistiques détaillées de vos performances"
            actionText="Voir Demo"
            actionColor="info"
            onClick={handleVisualizationClick}
            disabled={true}
          />
          
          <FeatureCard
            icon={<Assessment sx={{ fontSize: 48 }} />}
            title="Analyse Complète"
            description="Rapports détaillés avec mention, décision et recommandations"
            actionText="Analyser"
            actionColor="secondary"
            onClick={handleAnalysisClick}
            disabled={true}
          />
          
          <FeatureCard
            icon={<GetApp sx={{ fontSize: 48 }} />}
            title="Export PDF"
            description="Téléchargez votre bulletin officiel au format PDF"
            actionText="Exporter"
            actionColor="success"
            onClick={handleExportClick}
            disabled={true}
          />
        </Box>
      </Box>

      {/* Section Info */}
      <Box 
        sx={{ 
          textAlign: 'center', 
          py: 6, 
          backgroundColor: 'primary.50',
          borderRadius: 3,
          border: '1px solid',
          borderColor: 'primary.100'
        }}
      >
        <School sx={{ fontSize: 60, color: 'primary.main', mb: 2 }} />
        <Typography variant="h5" gutterBottom color="primary.main">
          Conforme au Système UY1
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 500, mx: 'auto' }}>
          Notre calculateur utilise les règles officielles de calcul de MGP de l'Université de Yaoundé 1
          pour garantir des résultats précis et conformes.
        </Typography>
      </Box>
    </Container>
  );
}