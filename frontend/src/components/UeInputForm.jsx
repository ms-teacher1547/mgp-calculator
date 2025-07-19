// src/components/UeInputForm.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Button,
  TextField,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Box,
  IconButton,
  Chip,
  Alert,
  Fade,
  LinearProgress,
  MenuItem,
  Tooltip,
  Card,
  CardContent,
  Divider
} from '@mui/material';
import {
  Add as AddIcon,
  Delete as DeleteIcon,
  Calculate as CalculateIcon,
  Person as PersonIcon,
  School as SchoolIcon,
  Info as InfoIcon
} from '@mui/icons-material';
import api from '../services/api';

export default function UeInputForm() {
  const navigate = useNavigate();
  const [ues, setUes] = useState([
    { nom: '', credits: 6, note: 0 }
  ]);
  const [studentName, setStudentName] = useState('');
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [showPreview, setShowPreview] = useState(false);

  // Validation en temps réel
  const validateField = (field, value, index = null) => {
    const newErrors = { ...errors };
    
    if (field === 'studentName') {
      if (!value.trim()) {
        newErrors.studentName = 'Le nom de l\'étudiant est requis';
      } else {
        delete newErrors.studentName;
      }
    }
    
    if (field === 'ue' && index !== null) {
      const ueKey = `ue_${index}`;
      if (!value.nom.trim()) {
        newErrors[ueKey] = 'Le nom de l\'UE est requis';
      } else if (value.note < 0 || value.note > 100) {
        newErrors[ueKey] = 'La note doit être entre 0 et 100';
      } else {
        delete newErrors[ueKey];
      }
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleStudentNameChange = (e) => {
    const value = e.target.value;
    setStudentName(value);
    validateField('studentName', value);
  };

  const handleUeChange = (index, field, value) => {
    const newUes = [...ues];
    newUes[index][field] = field === 'note' ? (parseFloat(value) || 0) : 
                           field === 'credits' ? parseInt(value) : value;
    setUes(newUes);
    validateField('ue', newUes[index], index);
    
    // Afficher l'aperçu si on a au moins une UE valide
    const hasValidUe = newUes.some(ue => ue.nom.trim() && ue.note >= 0);
    setShowPreview(hasValidUe && studentName.trim());
  };

  const handleAddUe = () => {
    setUes([...ues, { nom: '', credits: 6, note: 0 }]);
  };

  const handleRemoveUe = (index) => {
    if (ues.length > 1) {
      const newUes = ues.filter((_, i) => i !== index);
      setUes(newUes);
      // Nettoyer les erreurs pour cette UE
      const newErrors = { ...errors };
      delete newErrors[`ue_${index}`];
      setErrors(newErrors);
    }
  };

  const calculatePreviewMGP = () => {
    const validUes = ues.filter(ue => ue.nom.trim() && ue.note >= 0);
    if (validUes.length === 0) return 0;
    
    const totalPoints = validUes.reduce((sum, ue) => sum + (ue.note * ue.credits), 0);
    const totalCredits = validUes.reduce((sum, ue) => sum + ue.credits, 0);
    return totalCredits > 0 ? (totalPoints / totalCredits).toFixed(2) : 0;
  };

  const getProgressColor = (mgp) => {
    if (mgp >= 70) return 'success';
    if (mgp >= 60) return 'warning';
    return 'error';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation finale
    const isStudentNameValid = validateField('studentName', studentName);
    const validUes = ues.filter(ue => ue.nom.trim() !== '');
    
    if (!isStudentNameValid || validUes.length === 0) {
      setErrors(prev => ({
        ...prev,
        general: 'Veuillez remplir tous les champs requis'
      }));
      return;
    }

    setLoading(true);
    try {
      const response = await api.calculateMGP(validUes, studentName);
      navigate('/result', { state: { result: response } });
    } catch (error) {
      console.error('Erreur:', error);
      setErrors({ general: 'Erreur lors du calcul. Veuillez réessayer.' });
    } finally {
      setLoading(false);
    }
  };

  const previewMGP = calculatePreviewMGP();
  const progressValue = Math.min((previewMGP / 100) * 100, 100);

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto' }}>
      {/* En-tête du formulaire */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <SchoolIcon sx={{ mr: 2, color: 'primary.main' }} />
            <Typography variant="h5" component="h2">
              Saisie des Notes
            </Typography>
          </Box>
          
          <Typography variant="body2" color="text.secondary">
            Entrez vos informations et notes pour calculer votre MGP automatiquement
          </Typography>
        </CardContent>
      </Card>

      <form onSubmit={handleSubmit}>
        {/* Informations étudiant */}
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <PersonIcon sx={{ mr: 2, color: 'primary.main' }} />
              <Typography variant="h6">Informations Étudiant</Typography>
            </Box>
            
            <TextField
              label="Nom complet de l'étudiant"
              value={studentName}
              onChange={handleStudentNameChange}
              fullWidth
              required
              error={!!errors.studentName}
              helperText={errors.studentName}
              placeholder="Ex: KAMDEM Jean Pierre"
              sx={{ mb: 2 }}
            />
          </CardContent>
        </Card>

        {/* Tableau des UE */}
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
              <Typography variant="h6">Unités d'Enseignement</Typography>
              <Chip 
                label={`${ues.filter(ue => ue.nom.trim()).length} UE${ues.filter(ue => ue.nom.trim()).length > 1 ? 's' : ''}`}
                color="primary"
                size="small"
              />
            </Box>

            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: 600 }}>Nom de l'UE</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Crédits</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Note (/100)</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {ues.map((ue, index) => (
                    <TableRow key={index} sx={{ '&:hover': { backgroundColor: 'action.hover' } }}>
                      <TableCell>
                        <TextField
                          placeholder="Ex: Mathématiques I"
                          value={ue.nom}
                          onChange={(e) => handleUeChange(index, 'nom', e.target.value)}
                          fullWidth
                          variant="outlined"
                          size="small"
                          error={!!errors[`ue_${index}`]}
                          required
                        />
                      </TableCell>
                      <TableCell>
                        <TextField
                          select
                          value={ue.credits}
                          onChange={(e) => handleUeChange(index, 'credits', e.target.value)}
                          fullWidth
                          size="small"
                          variant="outlined"
                        >
                          <MenuItem value={3}>3 crédits</MenuItem>
                          <MenuItem value={6}>6 crédits</MenuItem>
                        </TextField>
                      </TableCell>
                      <TableCell>
                        <TextField
                          type="number"
                          inputProps={{ min: 0, max: 100, step: 0.1 }}
                          value={ue.note}
                          onChange={(e) => handleUeChange(index, 'note', e.target.value)}
                          fullWidth
                          size="small"
                          variant="outlined"
                          error={ue.note < 0 || ue.note > 100}
                          helperText={ue.note < 0 || ue.note > 100 ? 'Entre 0 et 100' : ''}
                        />
                      </TableCell>
                      <TableCell>
                        <Tooltip title={ues.length > 1 ? "Supprimer cette UE" : "Au moins une UE requise"}>
                          <span>
                            <IconButton
                              onClick={() => handleRemoveUe(index)}
                              disabled={ues.length === 1}
                              color="error"
                              size="small"
                            >
                              <DeleteIcon />
                            </IconButton>
                          </span>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>

            <Button
              onClick={handleAddUe}
              startIcon={<AddIcon />}
              variant="outlined"
              sx={{ mt: 2 }}
            >
              Ajouter une UE
            </Button>
          </CardContent>
        </Card>

        {/* Aperçu en temps réel */}
        <Fade in={showPreview}>
          <Card sx={{ mb: 3, bgcolor: 'primary.50', border: '1px solid', borderColor: 'primary.200' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <InfoIcon sx={{ mr: 2, color: 'primary.main' }} />
                <Typography variant="h6" color="primary.main">
                  Aperçu du Calcul
                </Typography>
              </Box>

              <Box sx={{ mb: 2 }}>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  MGP Provisoire: <strong>{previewMGP}/100</strong>
                </Typography>
                <LinearProgress
                  variant="determinate"
                  value={progressValue}
                  color={getProgressColor(previewMGP)}
                  sx={{ height: 8, borderRadius: 4 }}
                />
              </Box>

              <Typography variant="body2" color="text.secondary">
                Total crédits: {ues.filter(ue => ue.nom.trim()).reduce((sum, ue) => sum + ue.credits, 0)}
              </Typography>
            </CardContent>
          </Card>
        </Fade>

        {/* Messages d'erreur */}
        {errors.general && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {errors.general}
          </Alert>
        )}

        {/* Bouton de soumission */}
        <Card>
          <CardContent>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              size="large"
              startIcon={<CalculateIcon />}
              disabled={loading || Object.keys(errors).length > 0}
              fullWidth
              sx={{ py: 1.5 }}
            >
              {loading ? 'Calcul en cours...' : 'Calculer la MGP'}
            </Button>
            
            {loading && (
              <LinearProgress sx={{ mt: 2 }} />
            )}
          </CardContent>
        </Card>
      </form>
    </Box>
  );
}