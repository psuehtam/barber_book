import { useEffect, useState } from 'react'
import './App.css'

type HealthResponse = {
  status: string
  timestamp: string
}

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export default function App() {
  const [health, setHealth] = useState('Verificando API...')

  useEffect(() => {
    fetch(`${API_URL}/health`)
      .then((response) => {
        if (!response.ok) {
          throw new Error('A API respondeu com erro')
        }
        return response.json() as Promise<HealthResponse>
      })
      .then((data) => setHealth(`API: ${data.status}`))
      .catch(() => setHealth('API indisponível'))
  }, [])

  return (
    <main className="page">
      <section className="hero">
        <p className="eyebrow">Agendamento simples</p>
        <h1>BarberBook</h1>
        <p>
          Escolha um serviço, um barbeiro e o melhor horário.
        </p>
        <span className="health">{health}</span>
      </section>
    </main>
  )
}
