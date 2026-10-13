function SearchInput({ placeholder, value, onChange, onEnter, autoFocus }) {
  return (
    <div className="search-box">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
        <circle cx="11" cy="11" r="7" />
        <path d="m20 20-3.5-3.5" />
      </svg>
      <input
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        autoFocus={autoFocus}
        onKeyDown={(e) => e.key === 'Enter' && onEnter?.(e.target.value)}
      />
    </div>
  );
}

export default SearchInput;
