import { useState } from 'react';

/**
 * Hook para gestionar el estado de un formulario con validación en tiempo real.
 *
 * @param {object} initialValues - Valores iniciales del formulario
 * @param {function} validate    - (values) => errorsObject — función de validación pura
 * @param {object}  transforms   - Mapa de transformaciones por campo, p.ej. { centroEducativo: v => ({ id: v }) }
 */
export function useForm(initialValues, validate, transforms = {}) {
  const [values, setValues] = useState(initialValues);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    let newValue = type === 'checkbox' ? checked : value;
    if (transforms[name]) newValue = transforms[name](newValue);

    const next = { ...values, [name]: newValue };
    setValues(next);

    if (touched[name] && validate) {
      const errs = validate(next);
      setErrors(prev => ({ ...prev, [name]: errs[name] || '' }));
    } else if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  // Marca el campo como tocado y valida al salir del campo
  const handleBlur = (e) => {
    const { name } = e.target;
    setTouched(prev => ({ ...prev, [name]: true }));
    if (validate) {
      const errs = validate(values);
      setErrors(prev => ({ ...prev, [name]: errs[name] || '' }));
    }
  };

  // Valida todos los campos (se llama en onSubmit)
  const validateAll = () => {
    if (!validate) return true;
    const allErrors = validate(values);
    setErrors(allErrors);
    return !Object.values(allErrors).some(Boolean);
  };

  return {
    values,
    setValues,
    errors,
    setErrors,
    touched,
    setTouched,
    handleChange,
    handleBlur,
    validateAll,
  };
}
