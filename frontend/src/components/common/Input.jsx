import { useState } from 'react';

const Input = ({
  id,
  name,
  type = "text",
  label,
  placeholder,
  value,
  onChange,
  onBlur: externalOnBlur,
  error,
  required = false,
  disabled = false,
  className = "",
  ...rest
}) => {
  const [touched, setTouched] = useState(false);

  const handleBlur = (e) => {
    setTouched(true);
    externalOnBlur?.(e);
  };

  // Borde verde tras primer blur si el campo tiene valor y no hay error
  const isValid = touched && !error && Boolean(value);

  return (
    <div className="w-full">
      {label && (
        <label
          htmlFor={id}
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          {label}
          {required && <span className="text-red-500 ml-1">*</span>}
        </label>
      )}

      <input
        id={id}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        onBlur={handleBlur}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        className={`
          w-full px-3 py-2 border rounded-md transition-colors
          focus:outline-none focus:ring-2
          disabled:bg-gray-100 disabled:cursor-not-allowed
          ${error
            ? 'border-red-500 focus:ring-red-400'
            : isValid
            ? 'border-green-500 focus:ring-green-400'
            : 'border-gray-300 focus:ring-brand-primary'}
          ${className}
        `}
        {...rest}
      />

      {error && (
        <p className="mt-1 text-sm text-red-600">
          {error}
        </p>
      )}
    </div>
  );
};

export default Input;
