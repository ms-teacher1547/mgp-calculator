// src/pages/HomePage.jsx
import { Container, Typography } from '@mui/material';
import UeInputForm from '../components/UeInputForm';

export default function HomePage() {
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom align="center">
        Calculateur MGP UY1
      </Typography>

      <UeInputForm />
    </Container>
  );
}
