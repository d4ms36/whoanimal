"""
Protocolo e interfaces para el servicio de identificación animal.
Permite desacoplar el motor concreto de IA (ONNX, PyTorch, APIs externas) del resto del sistema.
"""

from typing import Protocol, Any, Optional
from ..domain.models.animal import AnimalProfile


class IdentificationService(Protocol):
    """Contrato que deben satisfacer los motores de identificación de fauna."""

    def identify_from_image(self, image_data: Any) -> Optional[AnimalProfile]:
        """
        Analiza una imagen y retorna el perfil zoológico correspondiente si es identificado.
        
        Args:
            image_data: Objeto de imagen en memoria, bytes o ruta de archivo.
            
        Returns:
            AnimalProfile si la especie fue reconocida con suficiente confianza, None en caso contrario.
        """
        ...
