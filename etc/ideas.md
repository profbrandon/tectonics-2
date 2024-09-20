
Disclaimer: Many ideas are transferred to here from [Viilanen 2012]

# Simulation Ideas

Plates
  - Bounding boxes, i.e., 2D arrays of chunks
    + Not all of the array will be populated
    + Ideally, whenever a plate changes, we should want to recalculate the bounding box to minimize wasted space
      * Potentially, we could use the amount of waste to determine whether a plate should be partitioned
  - Mass, Velocity
    + To apply Newton's second law, we will need to know the COM, requiring us to integrate over the surface area
    + One way to ensure that there is no immobile state is to allow for small default accelerations in certain parts of the map, akin to the coriolis acceleration
    + Frictional forces determined by the mass of the plate
      * In reality, this should also depend on the shape of the underside of the plate
      * They could also depend on the velocity of the plate, that way plates don't accelerate indefinitely
  - Fracture boundaries (Weak points where the plate should split)
    + Should either be randomly decided or systematically chosen, but the latter is preferable
      * The thinner the crust, the easier it should be to split
      * Plates should be roughly circular, so fractures should promote this shape
        - One way to do this would be to convert plates into a collection of locals, build a graph of connections, and analyze the graph for low connectivity
        - Another way would be to progressively expand from the COM until the the ratio of added continent vs waste reaches some lower threshold
  - Interactions
    + It makes a lot of sense that [Viilanen 2012] kept a list of continents on plates. This means that collision types can be easily categorized
      * In reality, it's mostly plate density that dictates plate collisions, but this approach works since continental crust is, on average, less dense than oceanic crust
      * Perhaps the properties of some local region (the overlap) could be used to determine the status of collisions
    + Adhesion of the continental margins
      * Occurs after subduction, presumably when a sufficient amount of material (metamorphic rock) binds the two
        - This could be done by adding metamorphic rock to the bottom of the continent
        - It seems that this also happens via igneous intrusions, literally suturing the continents together
      * What should be the conditions for adhesion to occur?
        - It seems implausible that plates with radically different speeds would merge
        - There should be opposing forces to counter the plate's relative motion (i.e., a restorative force proportional to their speeds)
      * When the two plates combine, their center of mass and velocity will need to be recalculated from their total momentum
    + Subduction
      * One plate dives down below the other but most likely this should not be directly simulated
      * Causes accretion of islands, the folding of the riding plate, and the creation of deepsea trenches
      * How far should the plates be allowed to subduct under each other if at all?
        - The worry is that mountains would stack on one line of pixels bordering the continents
          + Perhaps through a combination of accretion and "folding" a similar result could be obtained
      * How should this affect the plate's momentum? Subduction is theorized to pull the hanging plate towards the margin, but should the riding plate slow down?
      * Can thrust faults be modeled in a somewhat realistic fashion?
        - Aside from thrust belts, the subduction could be partially modeled by some special aggregation function
        - A first approach could be to stack weaker layers (up to some maximum) and to inherit the basement of the riding plate (mimicking "thin skinned" tectonics)
    + Divergence
      * Creates new oceanic crust or crust of decreasing height to some minimum value (probably the latter)
        - In the case of the latter, should there be a drop off when the crust becomes submerged?
        - How can this be used to model normal faults in a semi-realistic fashion?
      * In the ocean it creates midocean ridges which are explained by the less-dense, freshly created rock that then cools and accumulates rock from the upper mantle
        - This could potentially be modeled but would either need to use age to determine the accretion, or rock temperature (the former seems more appealing)
      * This should push plates apart, providing an acceleration, possibly related to the length of the boundary?
    + Transform Boundaries
      * These only really make sense when the plates are moving in exactly opposite directions and if the boundary between them is parallel to their velocity vectors
        - This initially appears to be the case (as is mentioned in [Viilanen 2012]) but on further inspection, there will almost always be transform boundaries. This is because if you consider one plate's frame of reference, then the other is always moving in some other direction. So long as one part of the plate's shared boundary is aligned with this vector then there is a transform fault
      * How could these be modeled? Is leaving them out inaccurate?
        - It seems likely that the action of plates would try to minimize the energy between them, thus, plate boundaries should optimize themselves for either purely transform behavior or purely subductive behavior. Perhaps, plates could lose small bits of material to the other, slowly changing the orientation of the boundary. What could be the local mechanism to do this?

Chunks
  - Contain rock type, density, age, erodibility, height (depth?) etc.
    + Obviously, rock type, density, and erodibility should all be correlated
      * Rock (Layer) Types:
        - Granular (Extremely erosive)
          + Soil
            * Fertility might be difficult to model
          + Sand
          + Gravel
          + Ash
        - Sedimentary (Erosive)
          + Limestone
            * Built by precipitating calcium carbonate largely controled by the amount of CO2 in the water
          + Mudstone
          + Sandstone
          + Shale
          + Conglomerate
          + Tuff
        - Igneous (Hardly erosive)
          + Basalt -> Intrusive Gabbro
            * Low viscosity
            * Flows over a larger area, building shield volcanoes
            * Harder than other igneous rocks
          + Andesite -> Intrusive Diorite
            * Medium viscosity
            * Flows over a small area, building stratovolcanoes
            * Also produces smaller tephra deposits
          + Rhyolite -> Intrusive Granite
            * High viscosity
            * Explodes, creating calderas and depositing tephra
        - Metamorphic (Moderately erosive)
          + While there are many types of metamorphic rock, it might make sense to treat them as one type of rock
          + The conversion from normal rock to metamorphic should depend on a compression factor and it should preserve some aspects of the parent rock
          + Maybe this should just be a boolean?
    + Could potentially contain layers
      * Should this model the "floating" of the crust on the mantle, if so, the oceans will need to be dealt with in a similar manner
  - Maybe fertility? As in how much nutrients are present in the rock (which derived soils inherit)

Erosion
  - Mass should be conserved by this process, and so whatever is eroded should be deposited on an adjacent chunk
  - Would most likely look like a blur with random perturbations
  - Erosion does not occur on the same scale under the ocean, so it could be ignored or at least slowed there
  - Possibly could be allowed to vary depending on other values, e.g., weathering, vegetation, and height
    + Weathering includes several types of erosion by wind, heating, that due to various states of water, and landslides
      * Wind: this would be difficult to model for the simple reason that it would involve simulating air
      * Heating: this also doesn't make much sense to model due to its lack of uniformity
      * Liquid water: this one is also difficult to model without more study done on fluid mechanics, but there are potential workarounds
        - Rivers almost always empty into the ocean, i.e., the boundaries of the continents, imposing the condition that there should be no local minima on the interior of a continent
          + Local minima should fill themselves. A simple blur would act in this manner, but it would only flatten the terrain, which is unrealistic, as erosion often takes flat landscapes and carves canyon systems into them.
            * This, of course, is a local property that should beget a local manipulation
          + Lower points collect more water, and so erosion is more advanced in those areas
        - Coastal erosion due to waves (which could be affected by wind)
          + This is also affected by how shallow the ocean is ahead of the shore
      * Ice: glaciers can only exist where it is cold enough, and so this could either depend on height or temperature (the prior is preferrable)
        - This would be modeled in similar way to rivers, but it would work faster, as glaciers do
      * Landslides: this would be modeled to a less accurate degree by a blur, but it should really take into account sediment type and height relative to adjacent chunks
    + Vegetation, to be accurately modeled, would need some kind of climate simulation, but could possibly be done with a combination of height and sediment
      * If climate was modeled, then different types of vegetation could be accounted for, but maybe this could just be represented in the density parameter
      * Height and sediment type would approximate for precipitation levels/climate
    + Height, as discussed above, would increase the possibility of glaciers and landslides, and decrease the amount of vegetation present
      * Potentially, soil production could be simulated as a combination of sand and vegetation combined with erosion
        - With this, vegetation could be rated by density and would be severely limited without soil, thus allowing for the development of forests and grasslands
  - Massive Landslides?

Bodies of Water
  - The amount of water in the model should ideally not change, but should be an initial parameter
    + Ice would need to be modeled physically, to reduce the amount of water in the oceans
    + The problem with this approach is determining what the height of the oceans should be
      * One would need to progressively "fill" the ocean by ordering all in order of height. This could be done but it might be unnecessarily expensive to run every frame
  - Alternatively, height level could be fixed for oceans
    + This, as an alternative, is easy, but would reduce the accuracy considerably
  - Lakes could be modeled here, with their water allocated first, and would require local minima and fill up to the point of overflowing

Volcanism
  - Hot spots could be modeled as certain regions that deposit igneous rock on the surface with long lifespans
    + These volcanoes would be fixed to the world map
  - Back-arc volcanism could be modeled similarly but with short lifespans
    + These volcanoes would need to travel with the continent
  - The viscosity of the magma could be initially set for a hot spot determining how large the deposition radius is
    + This would also determine the kind of rock deposited
    + Potentially, at the end of the intrusion's life, it could deposit some kind of intrusive igneous rock underground
      * This wouldn't need to be modeled all that accurately. I'm imagining that when the episode is finished, some portion of the preexisting rock is converted to intrusive igneous rock of the correct type

Climate
  - One way to model this would be to create default heat maps, akin to how the equator is perpetually warmer than the poles
    + The default heat map could then be perturbed by small random fluctuations and by the presence of different landforms
      * Mountains are areas of cooler air
      * Land in general is hotter than the ocean (this is potentially a bad approximation)
      * Vegetation reduces surface temperature
    + Based on this heat map, wind patterns could be approximated by integral curves in the gradient of the surface heat function (warm air rises, cool air rushes in to fill it's place)
      * Wind patterns could then be used to approximate precipitation by determining how much moisture is collected (via surface conditions like water and vegetation), and deposited (via mountain ranges)
      * Obviously, it could be difficult to create the integral curves, but could it be approximated in a cheap way?
    + Another way to model this would be to compute "boundaries" between sufficiently hot and cold air, and use these boundaries to determine precipitation
    + In a similar way, this could be used to model ocean currents, where depth determines how cool the water is
      * This seems maybe less important for the scope of this project, because even though it affects the weather, it is not the driving force behind the weather
  - Greenhouse gases
    + Volcanism is the main contributor
      * How does one keep hot spot volcanism from introducing more and more carbon into the atmosphere?
    + Oceans and vegetation are the main sinks
    + It doesn't make much sense to try and model how they move in the atmosphere, but one thing to notice is that carbon dioxide accumulates at the poles, possibly due to global wind patterns
      * This should mean that greenhouse gases are concentrated around the colder areas, warming them, but vegetation would reduce this affect

Vegetation
  - As previously stated, vegetation should have some density level which depends on several other factors:
    + Soil/Rock type
    + Temperature
    + Height
      * But also local height profiles, i.e., vegetation grows along rivers and oceans
    + Precipitation
    + Carbon Dioxide
    + Proximity to oceans (height above oceans?)
    + Proximity to other vegetation
  - Should there be oceanic vegetation? If so, it would probably need to be interpeted as on the ocean floor, not at the surface
  - As discussed in other places, vegetation has an effect on the surrounding parameters:
    + Reduces heat
    + Converts sand into soil
    + Reduces local CO2 (should this be what reduces heat?)
    + Reduces erosion of the underlying soil and rock

The World Map
  - Topology
    + Spherical
      * There is no equidistant tesselation of the sphere for greater than 20 points, so this would have to be done differently
    + Toroidal (Quotient of R2 over ZxR U RxZ under the standard metric)
      * This is the easiest topology to implement
    + Toroidal (S1 x S1)
      * Similar to the sphere, there probably is not a equidistant collection of vertices for high numbers of points
    + The topology would need to account for vector transport, or could be partially encoded through "acceleration" terms (via the christoffel symbols)
      * This would require precomputing a lot of information about the topology
  - Scale
    + If we're trying to model an entire planet's worth of surface area (Earth's is 500,000,000 km^2), then even a 2048x1024 pixel map would have each pixel representing 240 km^2, in other words, it would be about 16km on a side
    + A 2048x1024 pixel map where pixels are 1km on a side is roughly 2,000,000 km^2 which is less than the surface area of the dwarf planet Ceres


# Display Ideas

Rendering Modes
  - For each type of variable, there should be some way to view it in isolation over the whole domain
    + Some of the variables might include temperature, height, rock type, vegetation, erosive factor, precipitation, etc.
  - It should be possible to zoom in and out in any of these modes
    + For zooming out, should it be able to go past single-pixels? If so, some kind of pixel aggregation will need to be done
  - In addition to a console mode, the user should have the ability to set how often (if at all) the landscape renders to the screen
  - The program should be able to load previous runs and display them
  - Pausing/stepping through the playback/simulation should be allowed

Parameters
  - The user should have the ability to change parameters, but they should become locked once the simulation is started

Output
  - There should be an option to determine whether intermediate states are saved to disk
    + These could have several different file formats depending on the use
      * If used for a simpler purpose, this could be a sequence of grayscale images representing the parameter fields
        - Maybe the user could select which of these should be saved
      * (If in Java) They could be entire Java objects, but this seems clumsy



# Parameter Space 

Chunk
  - Rock Layer(s)
    + Composition
      * Metamorphic Boolean?
      * Oceanic Origin Boolean?
      * Fertility
      * Erodibility (Hardness?)
      * Density
      * Age
    + Original Thickness
    + Compression Factor
  - Vegetation Density
    + Type?
  - Water Thickness (Depth)
    + Temperature?
    + Absorbed CO2?
  - Ice Thickness
  - Surface Temperature
  - Atmospheric CO2 content
  - Height (Aggregate)
  - Volcano Present
  - Derived Mass

World
  - Plates
    + Continents/Islands
    + Bounding Box
    + Mass (COM)
      * Column Mass: includes water content
    + Velocity
    + Acceleration
      * External Forces
        - Friction
        - Boundary conditions
    + Boundary
    + Fracture Zones
      * Weakness Plot
  - Margins
    + Divergent
    + Convergent
    + Transform?
  - Base Heat Map
    + Terrestrial
    + Oceanic?
    + Derived wind patterns
  - Base Accelerations Map
  - Hot Spot Volcanoes

Volcanoes
  - Position (relative to parent)
  - Age
  - Lifespan
  - Viscosity
  - Size
      


# Phases

## Phase 1

This will attempt to replicate the results presented in [Viitanen 2012]. As a result, much of the more complicated structure will be presently eliminated (reserved for future implementation). The subset of the parameters to be implemented are:

Chunk
  - Rock Layer(s)
    + Age
    + Original Thickness
  - Height (Aggregate)
  - Derived Mass

World
  - Plates
    + Continents/Islands
    + Bounding Box
    + Mass (COM)
    + Velocity
    + Acceleration
      * External Forces
        - Friction
        - Boundary conditions
    + Boundary
  - Margins
    + Divergent
    + Convergent

There are a few remarks to make about this list:
  1. Rock layers will be modeled, but they will be indistinguishable
  2. Erosion will be modeled via a simple blur
  3. Water thickness will be modeled as a constant height value
  4. Continents will be defined by whatever is above water as opposed to stratigraphy
  5. Divergent margins will simply make new crust at decreasing values, with no special boundary modifications
  6. Convergent margins will just be overlapping pieces that eventually deposite crust underneath existing crust (inland)
  7. In line with [Viilanen 2012], if plate motion slows down too much, then the plates are given new random velocities
  8. In Ocean-Ocean collisions Continent-Continent collisions, the "subducted" plate is arbitrarily chosen

The goal of this phase is mainly to produce results similar to those in the above mentioned paper. The first aspect that should be worked on is that of the GUI. It would be best to figure out all of the magnification, translation, and viewing window mechanics, as well as provide basic simulation control. The GUI should allow for the following to be adjusted:
  - Map Properties
    + Width
    + Length
    + Allowable Height
  - Viewing Properties
    + Center Location
    + Level of Magnification
    + Query Tool
      * Height profile (linear application of the query tool)
  - Adjustable Parameters
    + Initial Sea Level Height
    + Transfer Ratio (_folding_ratio in [Viilanen 2012])
    + Continental Merging Ratio (agg_ratio_rel in [Viilanen 2012])


## Phase 2

The goal of this phase is to provide support for stratigraphy, volcanism, and realistic erosion. The introduction of different rock layers is a large step in the direction of realism, but with it raises several questions:

  1. How can discretized plate interactions be modeled in the stratigraphic record?
    - Can subduction (thrust belts, accretionary wedges, metamorphism, reverse faulting of bedrock) be modeled in a realistic/computationally inexpensive fashion?
    - How can one model the transition from continental crust to oceanic along rifting margins?
    - To model this, does one need a representation of faults?
    - Does this introduce less realism by trying to discretize something too continuous?
    - Plates automatically organize themselves into two margins, those perpendicular to relative plate motion (extensional), and those parallel (transform). What kind of algorithm is needed to model this? How much realism is gained from an implementation?
  
  2. What level of detail is sufficient to represent different rock classes?
  
  3. When plates break up, what should determine the break?
    - Is it too computationally expensive to compute zones of weakness?
    - How could this algorithm guarantee that plates are not too misshapen?

What is perhaps more pressing than each of these individual questions is whether they can all be unified in a (reasonably) elegant framework. Volcanism should be the easiest of the three features to implement, as volcanoes themselves are not terribly complicated and so a simulated simplification of them should be reasonably straightforward. Since we will not be modeling precipitation in this phase, we will need some algorithm that artificially reproduces the effects of precipitation. Some features that would be excellent to have are:
  1. Mountainous regions with valleys and ridges
  2. Large semi-flat watersheds
  3. Canyons
  4. Depositional environments (deltas, alluvial fans)

The main issue is that these landforms require certain constraints to produce their forms:
  - Mountains need to erode quickly, which means high erosion, but they also erode in particular ways that leave tall ridges and steep valleys
  - For large basins (like the Mississippi River's watershed) high erosion and high deposition cancel each other out, acting more as a transportation system
  - For canyons to occur, there must be high erosion immediately along the river's path, and low erosion everywhere else (or at least equal deposition)
  - Deltas can only form if their depositional rate outpaces that of costal erosion

These constrains raise some interesting issues related to equalibrium and suggest that there will necessarily be some parameters that need fine-tuning in order to produce realistic topographies. Yet, nature has automatically picked these fine-tuned variables, which suggests another question: what principles guide nature toward the fine-tuning of these variables?

Different types of erosion should be modeled seperately in order to seperate out their logic, which can then be combined later as a pointwise sum. The main types of erosion we will be trying to model are that due to liquid water and that due to (small) landslides. For the former, there are two factors that initially appear to influence erosion: speed and volume of flow. River speed should be related to how steep the slope is and volume should roughly correspond to height. What is interesting is that river speed actually also follows height. The reason is mostly kinematic, as even though high erosion appears on steep slopes, this erosion steals kinetic energy from the water, reducing its speed: faster water is unencumbered water. This suggests the possibility that height should be the only terrain-based variable. This doesn't feel entirely satisfactory though, because otherwise canyons have no incentive to form. It seems obvious that canyons form because there is less erosion on the canyon's walls than is at the canyon's bottom. There is more water here and thus more erosion. What we are looking for are signs that it follows this pattern, i.e., the height profile of neighboring cells should look something like:

2  1  2          2  2  2          2  2  1
2  0  2    or    1  0  2    or    2  0  2
2 -1  2          2 -1  2          2 -1  2

The simplest possible algorithm for this would be to check whether the center cell is the second smallest cell (after checking that it isn't a local minimum). If this is the case, it could increase the erosion factor of the center cell.

One thing that this overlooks is the size of the watershed. It seems that this should have a huge impact on how much erosion should be done because it controls how much water is available for erosion. This suggests that maybe watersheds should be calculated. For every square marked as a river square, the upriver watershed could be calcuated, determining how much erosion is to be done at that point. The nice thing about this is that only the boundary could possibly change, so that is what would need to be updated every cycle.

Landslides are much simpler, and should really act as a parameterized blur (with the parameter being the hardness of the underlying rock). 

The only other issue to address about erosion is that it is two-fold: erosion is deposition; mass is conserved. In other words, the erosion algorithm must not only figure out how much to decrease a chunks height by. It must also determine which chunks should receive the eroded material. In either case, the material should be deposited on any chunk lower than the target chunk. The material could be partitioned based on how much lower they are. Lastly, the status of this material should be determined. For example, landslides might take rock and reduce it to gravel, or maybe the rock just slides down unaffected. Or, water may take gravel and turn it into sand. One way to do this could be to keep track of how much the material has been subjected to the erosion algorithm. An internal counter for the layer could keep track of how many erosion periods it has left before it becomes the next stage of material.