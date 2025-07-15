import { Container, Typography, Button, Box } from '@mui/material';
import ResultDisplay from '../components/ResultDisplay';
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

export default function ResultPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const result = location.state?.result;

  useEffect(() => {
    if (!result) {
      navigate('/');
    }
  }, [result, navigate]);

  const handleBackToForm = () => {
    navigate('/');
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" align="center" gutterBottom>
        Résultat du Calcul de MGP
      </Typography>

      {result && <ResultDisplay result={result} />}

      <Box sx={{ mt: 4, display: 'flex', justifyContent: 'center' }}>
        <Button
            variant="contained"
            color="secondary"
            startIcon={<ArrowBackIcon />}
            onClick={handleBackToForm}
            >
            Calculer un autre MGP
        </Button>
      </Box>
    </Container>
  );
}
