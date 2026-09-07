import json
import uuid
import os
import glob

# Try to keep the existing UUIDs by reading existing files if possible
existing_ids = {}
existing_files = glob.glob("data/species/*.json")
for file in existing_files:
    with open(file, 'r', encoding='utf-8') as f:
        data = json.load(f)
        if data and isinstance(data, list) and len(data) > 0:
            existing_ids[data[0]["scientific_name"]] = data[0]["animal_id"]

species_list = [
    # Mammals (8)
    ("Canis lupus familiaris", "Perro doméstico", "Animalia", "Chordata", "Mammalia", "Carnivora", "Canidae", "Canis", "NE", False, 
     "Áreas habitadas por humanos a nivel global", "Omnívoro", 13, 60, 20.0, "Diurno", ["Global"]),
    ("Felis catus", "Gato doméstico", "Animalia", "Chordata", "Mammalia", "Carnivora", "Felidae", "Felis", "NE", False,
     "Áreas habitadas por humanos a nivel global", "Carnívoro", 15, 46, 4.5, "Crepuscular", ["Global"]),
    ("Panthera onca", "Jaguar", "Animalia", "Chordata", "Mammalia", "Carnivora", "Felidae", "Panthera", "NT", False,
     "Bosques tropicales y selvas", "Carnívoro", 15, 170, 95.5, "Diurno", ["América del Sur", "América Central"]),
    ("Ailuropoda melanoleuca", "Panda gigante", "Animalia", "Chordata", "Mammalia", "Carnivora", "Ursidae", "Ailuropoda", "VU", True,
     "Bosques templados de montaña", "Herbívoro (Bambú)", 20, 150, 100.0, "Diurno", ["Asia Central"]),
    ("Elephas maximus", "Elefante asiático", "Animalia", "Chordata", "Mammalia", "Proboscidea", "Elephantidae", "Elephas", "EN", False,
     "Bosques tropicales y pastizales", "Herbívoro", 60, 275, 4000.0, "Diurno", ["Asia del Sur", "Asia Sudoriental"]),
    ("Loxodonta africana", "Elefante africano", "Animalia", "Chordata", "Mammalia", "Proboscidea", "Elephantidae", "Loxodonta", "VU", False,
     "Sabanas y bosques", "Herbívoro", 70, 330, 6000.0, "Diurno", ["África subsahariana"]),
    ("Gorilla beringei", "Gorila oriental", "Animalia", "Chordata", "Mammalia", "Primates", "Hominidae", "Gorilla", "EN", True,
     "Bosques tropicales montanos", "Herbívoro", 40, 170, 160.0, "Diurno", ["África Oriental"]),
    ("Balaenoptera musculus", "Ballena azul", "Animalia", "Chordata", "Mammalia", "Cetartiodactyla", "Balaenopteridae", "Balaenoptera", "EN", True,
     "Océanos de todo el mundo", "Carnívoro (Krill)", 90, 2400, 150000.0, "Diurno", ["Océanos Globales"]),
    
    # Birds (6)
    ("Haliaeetus leucocephalus", "Águila calva", "Animalia", "Chordata", "Aves", "Accipitriformes", "Accipitridae", "Haliaeetus", "LC", False,
     "Zonas costeras y lagos", "Carnívoro", 20, 90, 4.5, "Diurno", ["América del Norte"]),
    ("Ara macao", "Guacamayo rojo", "Animalia", "Chordata", "Aves", "Psittaciformes", "Psittacidae", "Ara", "LC", False,
     "Selvas tropicales", "Herbívoro", 50, 85, 1.0, "Diurno", ["América Central", "América del Sur"]),
    ("Spheniscus magellanicus", "Pingüino de Magallanes", "Animalia", "Chordata", "Aves", "Sphenisciformes", "Spheniscidae", "Spheniscus", "NT", False,
     "Costas templadas", "Carnívoro", 25, 70, 4.0, "Diurno", ["América del Sur"]),
    ("Struthio camelus", "Avestruz", "Animalia", "Chordata", "Aves", "Struthioniformes", "Struthionidae", "Struthio", "LC", False,
     "Sabanas y desiertos", "Omnívoro", 40, 250, 100.0, "Diurno", ["África"]),
    ("Phoenicopterus roseus", "Flamenco común", "Animalia", "Chordata", "Aves", "Phoenicopteriformes", "Phoenicopteridae", "Phoenicopterus", "LC", False,
     "Lagunas y humedales", "Omnívoro", 30, 140, 3.0, "Diurno", ["Europa", "África", "Asia"]),
    ("Bubo bubo", "Búho real", "Animalia", "Chordata", "Aves", "Strigiformes", "Strigidae", "Bubo", "LC", False,
     "Bosques y acantilados", "Carnívoro", 20, 70, 2.5, "Nocturno", ["Europa", "Asia"]),
    
    # Reptiles (4)
    ("Iguana iguana", "Iguana verde", "Animalia", "Chordata", "Reptilia", "Squamata", "Iguanidae", "Iguana", "LC", False,
     "Selvas tropicales", "Herbívoro", 15, 150, 4.0, "Diurno", ["América Central", "América del Sur"]),
    ("Crocodylus niloticus", "Cocodrilo del Nilo", "Animalia", "Chordata", "Reptilia", "Crocodilia", "Crocodylidae", "Crocodylus", "LC", False,
     "Ríos y pantanos", "Carnívoro", 70, 450, 400.0, "Nocturno", ["África"]),
    ("Chelonia mydas", "Tortuga verde", "Animalia", "Chordata", "Reptilia", "Testudines", "Cheloniidae", "Chelonia", "EN", False,
     "Mares tropicales y subtropicales", "Herbívoro", 80, 120, 150.0, "Diurno", ["Océanos Globales"]),
    ("Ophiophagus hannah", "Cobra real", "Animalia", "Chordata", "Reptilia", "Squamata", "Elapidae", "Ophiophagus", "VU", True,
     "Bosques densos", "Carnívoro", 20, 400, 6.0, "Diurno", ["Asia del Sur", "Asia Sudoriental"]),
    
    # Amphibians (3)
    ("Agalychnis callidryas", "Rana arborícola verde", "Animalia", "Chordata", "Amphibia", "Anura", "Phyllomedusidae", "Agalychnis", "LC", False,
     "Selvas tropicales húmedas", "Carnívoro (Insectívoro)", 5, 7, 0.015, "Nocturno", ["América Central"]),
    ("Ambystoma mexicanum", "Ajolote", "Animalia", "Chordata", "Amphibia", "Urodela", "Ambystomatidae", "Ambystoma", "CR", True,
     "Lagos de gran altitud", "Carnívoro", 15, 23, 0.1, "Nocturno", ["América del Norte (México)"]),
    ("Dendrobates tinctorius", "Rana dardo venenosa", "Animalia", "Chordata", "Amphibia", "Anura", "Dendrobatidae", "Dendrobates", "LC", False,
     "Selvas tropicales húmedas", "Carnívoro (Insectívoro)", 10, 5, 0.008, "Diurno", ["América del Sur"]),
    
    # Fish (3)
    ("Carcharodon carcharias", "Gran tiburón blanco", "Animalia", "Chordata", "Chondrichthyes", "Lamniformes", "Lamnidae", "Carcharodon", "VU", False,
     "Aguas templadas y cálidas costeras", "Carnívoro", 70, 450, 1100.0, "Diurno", ["Océanos Globales"]),
    ("Amphiprion ocellaris", "Pez payaso", "Animalia", "Chordata", "Actinopterygii", "Perciformes", "Pomacentridae", "Amphiprion", "LC", False,
     "Arrecifes de coral", "Omnívoro", 10, 11, 0.05, "Diurno", ["Océano Indo-Pacífico"]),
    ("Hippocampus kuda", "Caballito de mar", "Animalia", "Chordata", "Actinopterygii", "Syngnathiformes", "Syngnathidae", "Hippocampus", "VU", False,
     "Arrecifes y estuarios", "Carnívoro", 4, 15, 0.02, "Diurno", ["Océano Indo-Pacífico"]),
    
    # Invertebrates (4)
    ("Danaus plexippus", "Mariposa monarca", "Animalia", "Arthropoda", "Insecta", "Lepidoptera", "Nymphalidae", "Danaus", "LC", False,
     "Bosques, prados y jardines", "Herbívoro", 1, 10, 0.001, "Diurno", ["América del Norte", "América Central"]),
    ("Apis mellifera", "Abeja melífera", "Animalia", "Arthropoda", "Insecta", "Hymenoptera", "Apidae", "Apis", "LC", False,
     "Prados, bosques y jardines", "Herbívoro (Néctar)", 1, 1, 0.0001, "Diurno", ["Global"]),
    ("Octopus vulgaris", "Pulpo común", "Animalia", "Mollusca", "Cephalopoda", "Octopoda", "Octopodidae", "Octopus", "LC", False,
     "Aguas costeras marinas", "Carnívoro", 2, 60, 3.0, "Nocturno", ["Océanos Globales"]),
    ("Theraphosa blondi", "Tarántula Goliat", "Animalia", "Arthropoda", "Arachnida", "Araneae", "Theraphosidae", "Theraphosa", "NE", True,
     "Bosques tropicales y pantanos", "Carnívoro", 20, 30, 0.17, "Nocturno", ["América del Sur"])
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

for s in species_list:
    sci_name, com_name, k, p, c, o, f, g, cons, rare, hab, diet, life, size, weight, cycle, regs = s
    
    # Add to taxonomy tree
    route = [k, p, c, o, f, g]
    add_to_tree(taxonomy_index, route)
    
    # Ensure stable UUID if it exists
    animal_id = existing_ids.get(sci_name, str(uuid.uuid4()))
    
    # Create JSON object
    data = [{
        "animal_id": animal_id,
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
        "is_rare_species": rare,
        "habitat": hab,
        "diet": diet,
        "lifespan_years": life,
        "size_cm": size,
        "weight_kg": weight,
        "activity_cycle": cycle,
        "native_regions": regs
    }]
    
    filename = sci_name.lower().replace(" ", "_") + ".json"
    filepath = os.path.join("data/species", filename)
    with open(filepath, 'w', encoding='utf-8') as file:
        json.dump(data, file, indent=2, ensure_ascii=False)

with open("data/taxonomy/taxonomy_index.json", "w", encoding='utf-8') as f:
    json.dump(taxonomy_index, f, indent=2, ensure_ascii=False)
