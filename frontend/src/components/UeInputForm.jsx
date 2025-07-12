// src/components/UeInputForm.jsx
import { useState } from 'react';
import { 
  Button, 
  TextField, 
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography
} from '@mui/material';
import api from '../services/api';

export default function UeInputForm({ onCalculate }) {
  const [ues, setUes] = useState([{ nom: '', credits: 6, note: 0 }]);
  const [studentName, setStudentName] = useState('');

  const handleAddUe = () => {
    setUes([...ues, { nom: '', credits: 6, note: 0 }]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.calculateMGP(
        ues.filter(ue => ue.nom.trim() !== ''),
        studentName
      );
      onCalculate(response.data);
    } catch (error) {
      console.error('Erreur:', error.response?.data);
      alert('Erreur lors du calcul');
    }
  };

  return (
    <Paper elevation={3} sx={{ p: 3, mb: 4 }}>
      <Typography variant="h6" gutterBottom>
        Saisie des Unités d'Enseignement
      </Typography>
      
      <TextField
        label="Nom de l'étudiant"
        value={studentName}
        onChange={(e) => setStudentName(e.target.value)}
        fullWidth
        margin="normal"
        required
      />

      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableBody>
            {ues.map((ue, index) => (
              <TableRow key={index}>
                <TableCell>
                  <TextField
                    label="Nom UE"
                    value={ue.nom}
                    onChange={(e) => {
                      const newUes = [...ues];
                      newUes[index].nom = e.target.value;
                      setUes(newUes);
                    }}
                    fullWidth
                    required
                  />
                </TableCell>
                <TableCell width="120px">
                  <TextField
                    select
                    label="Crédits"
                    value={ue.credits}
                    onChange={(e) => {
                      const newUes = [...ues];
                      newUes[index].credits = parseInt(e.target.value);
                      setUes(newUes);
                    }}
                    SelectProps={{ native: true }}
                    fullWidth
                  >
                    <option value={3}>3 crédits</option>
                    <option value={6}>6 crédits</option>
                  </TextField>
                </TableCell>
                <TableCell width="120px">
                  <TextField
                    type="number"
                    label="Note"
                    inputProps={{ min: 0, max: 100, step: 0.1 }}
                    value={ue.note}
                    onChange={(e) => {
                      const newUes = [...ues];
                      newUes[index].note = parseFloat(e.target.value) || 0;
                      setUes(newUes);
                    }}
                    fullWidth
                    required
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Button 
        onClick={handleAddUe} 
        variant="outlined" 
        sx={{ mt: 2, mr: 2 }}
      >
        Ajouter UE
      </Button>
      
      <Button 
        onClick={handleSubmit} 
        variant="contained" 
        color="primary" 
        sx={{ mt: 2 }}
      >
        Calculer MGP
      </Button>
    </Paper>
  );
}