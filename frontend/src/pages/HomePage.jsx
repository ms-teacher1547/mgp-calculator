// src/pages/HomePage.jsx
import { Container, Typography } from '@mui/material';
import UeInputForm from '../components/UeInputForm';
import ResultDisplay from '../components/ResultDisplay';
import { useState } from 'react';

export default function HomePage() {
  const [result, setResult] = useState(null);

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom align="center">
        Calculateur MGP UY1
      </Typography>
      
      <UeInputForm onCalculate={setResult} />
      {result && <ResultDisplay result={result} />}
    </Container>
  );
}