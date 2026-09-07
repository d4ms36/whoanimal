import json
import uuid
import os

species_list = [
    # Mammals (8)
    ("Canis lupus familiaris", "Perro doméstico", "Animalia", "Chordata", "Mammalia", "Carnivora", "Canidae", "Canis", "NE", False),
    ("Felis catus", "Gato doméstico", "Animalia", "Chordata", "Mammalia", "Carnivora", "Felidae", "Felis", "NE", False),
    ("Panthera onca", "Jaguar", "Animalia", "Chordata", "Mammalia", "Carnivora", "Felidae", "Panthera", "NT", False),
    ("Ailuropoda melanoleuca", "Panda gigante", "Animalia", "Chordata", "Mammalia", "Carnivora", "Ursidae", "Ailuropoda", "VU", True),
    ("Elephas maximus", "Elefante asiático", "Animalia", "Chordata", "Mammalia", "Proboscidea", "Elephantidae", "Elephas", "EN", False),
    ("Loxodonta africana", "Elefante africano", "Animalia", "Chordata", "Mammalia", "Proboscidea", "Elephantidae", "Loxodonta", "VU", False),
    ("Gorilla beringei", "Gorila oriental", "Animalia", "Chordata", "Mammalia", "Primates", "Hominidae", "Gorilla", "EN", True),
    ("Balaenoptera musculus", "Ballena azul", "Animalia", "Chordata", "Mammalia", "Cetartiodactyla", "Balaenopteridae", "Balaenoptera", "EN", True),
    
    # Birds (6)
    ("Haliaeetus leucocephalus", "Águila calva", "Animalia", "Chordata", "Aves", "Accipitriformes", "Accipitridae", "Haliaeetus", "LC", False),
    ("Ara macao", "Guacamayo rojo", "Animalia", "Chordata", "Aves", "Psittaciformes", "Psittacidae", "Ara", "LC", False),
    ("Spheniscus magellanicus", "Pingüino de Magallanes", "Animalia", "Chordata", "Aves", "Sphenisciformes", "Spheniscidae", "Spheniscus", "NT", False),
    ("Struthio camelus", "Avestruz", "Animalia", "Chordata", "Aves", "Struthioniformes", "Struthionidae", "Struthio", "LC", False),
    ("Phoenicopterus roseus", "Flamenco común", "Animalia", "Chordata", "Aves", "Phoenicopteriformes", "Phoenicopteridae", "Phoenicopterus", "LC", False),
    ("Bubo bubo", "Búho real", "Animalia", "Chordata", "Aves", "Strigiformes", "Strigidae", "Bubo", "LC", False),
    
    # Reptiles (4)
    ("Iguana iguana", "Iguana verde", "Animalia", "Chordata", "Reptilia", "Squamata", "Iguanidae", "Iguana", "LC", False),
    ("Crocodylus niloticus", "Cocodrilo del Nilo", "Animalia", "Chordata", "Reptilia", "Crocodilia", "Crocodylidae", "Crocodylus", "LC", False),
    ("Chelonia mydas", "Tortuga verde", "Animalia", "Chordata", "Reptilia", "Testudines", "Cheloniidae", "Chelonia", "EN", False),
    ("Ophiophagus hannah", "Cobra real", "Animalia", "Chordata", "Reptilia", "Squamata", "Elapidae", "Ophiophagus", "VU", True),
    
    # Amphibians (3)
    ("Agalychnis callidryas", "Rana arborícola verde", "Animalia", "Chordata", "Amphibia", "Anura", "Phyllomedusidae", "Agalychnis", "LC", False),
    ("Ambystoma mexicanum", "Ajolote", "Animalia", "Chordata", "Amphibia", "Urodela", "Ambystomatidae", "Ambystoma", "CR", True),
    ("Dendrobates tinctorius", "Rana dardo venenosa", "Animalia", "Chordata", "Amphibia", "Anura", "Dendrobatidae", "Dendrobates", "LC", False),
    
    # Fish (3)
    ("Carcharodon carcharias", "Gran tiburón blanco", "Animalia", "Chordata", "Chondrichthyes", "Lamniformes", "Lamnidae", "Carcharodon", "VU", False),
    ("Amphiprion ocellaris", "Pez payaso", "Animalia", "Chordata", "Actinopterygii", "Perciformes", "Pomacentridae", "Amphiprion", "LC", False),
    ("Hippocampus kuda", "Caballito de mar", "Animalia", "Chordata", "Actinopterygii", "Syngnathiformes", "Syngnathidae", "Hippocampus", "VU", False),
    
    # Invertebrates (4)
    ("Danaus plexippus", "Mariposa monarca", "Animalia", "Arthropoda", "Insecta", "Lepidoptera", "Nymphalidae", "Danaus", "LC", False),
    ("Apis mellifera", "Abeja melífera", "Animalia", "Arthropoda", "Insecta", "Hymenoptera", "Apidae", "Apis", "LC", False),
    ("Octopus vulgaris", "Pulpo común", "Animalia", "Mollusca", "Cephalopoda", "Octopoda", "Octopodidae", "Octopus", "LC", False),
    ("Theraphosa blondi", "Tarántula Goliat", "Animalia", "Arthropoda", "Arachnida", "Araneae", "Theraphosidae", "Theraphosa", "NE", True)
]

def add_to_tree(tree, route):
    current = tree
    for node in route:
        if node not in current:
            current[node] = {}
        current = current[node]

taxonomy_index = {}

os.makedirs("data/species", exist_ok=True)
os.makedirs("data/taxonomy", exist_ok=True)

# Remove old example.json if it exists
if os.path.exists("data/species/example.json"):
    os.remove("data/species/example.json")

for s in species_list:
    sci_name, com_name, k, p, c, o, f, g, cons, rare = s
    
    # Add to taxonomy tree
    route = [k, p, c, o, f, g]
    add_to_tree(taxonomy_index, route)
    
    # Create JSON object
    data = [{
        "animal_id": str(uuid.uuid4()),
        "scientific_name": sci_name,
        "common_name": com_name,
        "taxonomy": {
            "kingdom": k,
            "phylum": p,
            "class": c,
            "order": o,
            "family": f,
            "genus": g,
            "species": sci_name
        },
        "conservation_status": cons,
        "is_rare_species": rare
    }]
    
    filename = sci_name.lower().replace(" ", "_") + ".json"
    filepath = os.path.join("data/species", filename)
    with open(filepath, 'w', encoding='utf-8') as file:
        json.dump(data, file, indent=2, ensure_ascii=False)

with open("data/taxonomy/taxonomy_index.json", "w", encoding='utf-8') as f:
    json.dump(taxonomy_index, f, indent=2, ensure_ascii=False)
