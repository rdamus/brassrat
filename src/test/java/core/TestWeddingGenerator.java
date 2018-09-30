package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestWeddingGenerator {
	static Logger logger = Logger.getLogger(TestWeddingGenerator.class);
	static String BOATING = "Boating";
	static String RSVP = "RSVP";
	static String RSVPQTY = "RSVP Qty";
	static String NAME = "Name";
	static String MEGROB = "Meg and Rob";

	protected int totalBoatGuests;
	protected String[] headers;
	protected List<Guest> masterGuestList = new ArrayList<>();
	protected List<Guest> guests = new ArrayList<>();
	protected List<Boat> boats = new ArrayList<>();
	protected List<Guest> stragglers = new ArrayList<>();

	Path path;

	@Before
	public void setUp() throws Exception {
		path = Paths.get("c:\\", "Users", "rob", "Documents", "Wedding", "wedding_guests.csv");
		boats.add(createBoat(Boat.Type.Cat520, "No Regrets"));
		boats.add(createBoat(Boat.Type.Cat500, "Interstellar"));
		boats.add(createBoat(Boat.Type.Cat500, "Irie"));
		boats.add(createBoat(Boat.Type.Cat500, "Knot Bad"));
//		boats.add(createBoat(Boat.Type.Cat500, "Pleiades"));
		boats.add(createBoat(Boat.Type.Cat520, "Windborne"));
		boats.add(createBoat(Boat.Type.Cat520, "Osprey"));

	}

	@After
	public void tearDown() throws Exception {
	}

	Boat createBoat(Boat.Type type, String name) {
		return new Boat(type, name);
	}
	
	@Ignore
	public void testExcludes(){
		List<String> names = Arrays.asList("Mr. & Mrs. Andrew Marcinek", "Ms. Kerry Feeney");
		Set<String> set = new HashSet(Arrays.asList("Ms. Kerry Feeney"));
		assert !Collections.disjoint(names, set);
	}

	@Ignore
	public void testSplit() {
		System.out.println("split length: " + "1,2,3,4,5".split(",").length);
		System.out.println("split length: " + ",,3,4,5".split(",").length);
		System.out.println("split length: " + ",,3,4,5".split(",")[0]);
		System.out.println("split length: " + ",,3,4,5".split(",")[4]);
	}

	@Ignore
	public void testTotalBoatGuests() {
		List<String> responses = new ArrayList<>();
		// read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(path)) {
			// skip the header
			responses = stream.collect(Collectors.toList());
			headers = responses.get(0).split(",");

			responses.subList(1, responses.size()).forEach((line) -> {
				String[] vals = line.split(",");
				if (vals.length >= headers.length) {
					logger.debug(String.format("Vals.length %d, headers.length %d, Line %s", vals.length,
							headers.length, line));
					String guest = vals[indexOf(NAME)];
					String rsvp = vals[indexOf(RSVPQTY)];
					System.err.println("Guest " + guest + " rsvp qty: " + rsvp + " Boating: " + vals[indexOf(BOATING)]);
					if (vals[indexOf(BOATING)].equals("TRUE") && vals[indexOf(RSVP)].equals("Yes")) {
						logger.debug("Boating Guest " + guest + " rsvp qty: " + rsvp);
						totalBoatGuests += Integer.parseInt(rsvp);
					} else {
						logger.debug("Non Boating Guest " + guest + " rsvp qty: " + rsvp);
					}
				}
			});
			System.out.println("Total people on boats: " + totalBoatGuests);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	public void testBoatGuests() throws IOException {
		List<String> responses = generateResponses();
		responses.forEach(line -> parseGuestResponse(line));
		System.out.println("Total No. Guests: " + totalGuests());
		System.out.println("Total No. Guests Boating: "
				+ totalGuestsBoating());
		addGuestsToBoats();
		printBoats();
	}
	
	@Test
	public void testPreDeterminedBoatGuests() throws IOException {
		List<String> responses = generateResponses();
		responses.forEach(line -> parseGuestResponse(line));
		System.out.println("Total No. Guests: " + totalGuests());
		System.out.println("Total No. Guests Boating: " + totalGuestsBoating());
		//add us to the master list
		addMegAndRob();
		//generate a copy of the list
		guests.addAll(masterGuestList);
		
		addMatesForGuests();
		preAllocateBoats();
		addGuestsToBoats();
		handleStragglers();
		printBoats();
	}

	private void handleStragglers() {
		for (Guest g : stragglers) {
			boolean chosen = false;
			int kCnt = 0;
			int n = g.getQty();
			while (!chosen) {
				Boat b = randomBoat();
				if (b.hasRoomFor(n)) {
					b.addGuest(g);
					chosen = true;
				}else{
					System.err.println("tried " + ++kCnt + " times to seat straggler " + g);
				}
			}
		}
	}

	private void addMegAndRob() {
		masterGuestList.add(new Guest(true, MEGROB, 2));
	}

	private int totalGuests() {
		return masterGuestList.stream().mapToInt(g -> g.getQty()).sum();
	}

	private int totalGuestsBoating() {
		return masterGuestList.stream().filter(g -> g.isBoating()).mapToInt(g -> g.getQty()).sum();
	}

	private void addMatesForGuests() {
		Map<String,List<String>> mates = new HashMap<>();
		mates.put("Dr. & Mrs. Joseph Abularrage", Arrays.asList(
				"Mr. Bo Wallen & Ms. Danielle Jennings", 
				"Kenichi & Valeria Asano",
				"Mr. & Mrs. Hitesh Dheri"));
		mates.put("Uncle John & Leslie", Arrays.asList(
				"Mr. Bo Wallen & Ms. Danielle Jennings", 
				"Kenichi & Valeria Asano",
				"Mr. & Mrs. Hitesh Dheri"));
		mates.put(MEGROB, Arrays.asList(
				"Mr. Bo Wallen & Ms. Danielle Jennings", 
				"Kenichi & Valeria Asano",
				"David & Noreen Damus",
				"Mr. & Mrs. Hudson Harris",
				"Mike Damus & Jefa",
				"Dr.'s Edmund & Sarah Karam"));
		
		mates.forEach((k,v)->findGuest(k).addMates(v));
	}

	private void printBoats() {
		boats.forEach(b -> System.out.println("boat: " + b + " guests: " + b.getGuests()));
	}
	
	void preAllocateBoats(){
		//we're on the 520
		Guest megRob = findGuest(MEGROB);
		int idx = boats.indexOf(createBoat(Boat.Type.Cat520, "No Regrets"));
		boats.get(idx).addGuest(megRob);
		guests.remove(index(megRob));
		System.out.println("Adding " + megRob.getName() + " to " + boats.get(idx));
		//parents on a 500
		idx = boats.indexOf(createBoat(Boat.Type.Cat500, "Irie"));
		boats.get(idx).addGuest(findGuest("Dr. & Mrs. Joseph Abularrage"));
		boats.get(idx).addGuest(findGuest("Uncle John & Leslie"));
		guests.remove(index(findGuest("Dr. & Mrs. Joseph Abularrage")));
		guests.remove(index(findGuest("Uncle John & Leslie")));
		System.out.println("Adding Dr J & Co. to " + boats.get(idx));
		
		List<List<String>> l = Arrays.asList(
				Arrays.asList("Mr. & Mrs. Andrew Marcinek", "Ms. Kerry Feeney"),
				Arrays.asList("Soon to be Mr. & Mrs. Charles Schmidt", "Mr. & Mrs. Elias Roman"));
		for(List<String> couples:l){
			Boat b = randomBoat();
			while(!b.getGuests().isEmpty())
				b = nextBoat(b);
			for(String name:couples){
				System.out.println("Adding " + name + " to " + b);
				Guest g = findGuest(name);
				b.addGuest(g);	
				//remove guest from list
				guests.remove(index(g));
			}	
		}
	}

	private Guest findGuest(String name) {
		return guests.stream().filter(g->g.getName().equals(name)).findFirst().get();
	}
	
	private int index(Guest g) {
		for(int i = 0; i < guests.size(); i++){
			if( guests.get(i).getName().equals(g.getName()) )
				return i;
		}
		return -1;
	}

	
	void addGuestsToBoats() {
		List<Guest> boaters = guests.stream().filter(g -> g.isBoating()).collect(Collectors.toList());
		Collections.shuffle(boaters);
		//sort highest to lowest
		Collections.sort(boaters);
		Collections.reverse(boaters);
		

		for (Guest g : boaters) {
			boolean chosen = false;
			int kCnt = 0;
			while (!chosen) {
				Boat b = randomBoat();
				if (boatIsAvailable(g, b)) {
					b.addGuest(g);
					chosen = true;
				}else{
					System.err.println("tried " + ++kCnt + " times to seat guest " + g);
					if(kCnt > boaters.size()){
						stragglers.add(g);
						break;
					}
				}
			}
		}
		System.err.println("Could not seat: " + stragglers);
	}

	private boolean boatIsAvailable(Guest g, Boat b) {
		List<Guest> existingGuests = b.getGuests();
		boolean bOK = true;
		for(Guest existing:existingGuests){
			bOK &= existing.canSailWith(g.getName());
		}
		return b.hasRoomFor(g.getQty()) && bOK;
	}

	Boat randomBoat() {
		Random r = new Random();
		return boats.get(r.nextInt(boats.size()));
	}
	
	Boat nextBoat(Boat b) {
		Random r = new Random();
		
		while(true){
			Boat nextBoat = boats.get(r.nextInt(boats.size()));
			if(!nextBoat.equals(b))
				return nextBoat;
		}
	}

	List<String> generateResponses() throws IOException {
		List<String> responses = new ArrayList<>();
		Stream<String> stream = Files.lines(path);
		responses = stream.collect(Collectors.toList());
		headers = responses.get(0).split(",");
		stream.close();
		return responses.subList(1, responses.size());
	}

	void parseGuestResponse(String response) {
		String[] vals = response.split(",");
		if (vals.length >= headers.length) {
			String rsvp = vals[indexOf(RSVP)];
			if (rsvp.equalsIgnoreCase("yes")) {
				addGuest(vals);
			}
		}
	}

	void addGuest(String[] vals) {
		Guest g = createGuest(vals);

		System.err.println(g);
		if (g.isBoating()) {
			logger.debug("Boating Guest " + g.getName() + " rsvp qty: " + g.getQty());
			totalBoatGuests += g.getQty();
		} else {
			logger.debug("Non Boating Guest " + g.getName() + " rsvp qty: " + g.getQty());
		}
		masterGuestList.add(g);
	}

	private Guest createGuest(String[] vals) {
		String name = vals[indexOf(NAME)];
		int qty = Integer.parseInt(vals[indexOf(RSVPQTY)]);
		boolean boating = Boolean.parseBoolean(vals[indexOf(BOATING)]);
		return new Guest(boating, name, qty);
	}

	int indexOf(String str) {
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].trim().equals(str))
				return i;
		}
		return -1;
	}
}

class Boat {
	enum Type {
		Cat500, Cat520
	};

	Type type;
	String name;
	int capacity;
	List<Guest> guests = new ArrayList<>();

	public Boat(Type type, String name) {
		super();
		this.type = type;
		this.name = name;
		switch (type) {
		case Cat500:
			this.capacity = 8;
			break;
		case Cat520:
			this.capacity = 9;
			break;
		}
	}

	int qty() {
		int q = this.guests.stream().mapToInt(g -> g.getQty()).sum();
		// System.err.println("qty is " + q);
		return q;
	}

	boolean hasRoomFor(int n) {
		int newTotal = qty() + n;
		return newTotal <= capacity;
	}
	
	List<String> getGuestNames(){
		return guests.stream().map(g->g.getName()).collect(Collectors.toList());
	}

	public void addGuest(Guest g) {
		guests.add(g);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String toString() {
		return String.format("%s %s - (%d/%d)",type,name,qty(),capacity);
	}

	public List<Guest> getGuests() {
		return guests;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boat other = (Boat) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}

class Guest implements Comparable<Guest>{
	boolean boating;
	String name;
	int qty;
	Set<String> mates = new HashSet<>();

	public Guest(boolean boating, String name, int qty) {
		super();
		this.boating = boating;
		this.name = name;
		this.qty = qty;
	}

	public boolean canSailWith(String name) {
		return canSailWith(Arrays.asList(name));
	}

	public boolean canSailWith(List<String> names){
		if(mates.isEmpty()) return true;
		return !Collections.disjoint(mates, names);
	}
	
	public boolean isBoating() {
		return boating;
	}

	public void setBoating(boolean boating) {
		this.boating = boating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String toString() {
		return String.format("Guest: %s - qty(%d)",name,qty);
	}
	
	public void addMates(List<String> names){
		mates.addAll(names);
	}
	
	public void addMate(String name){
		mates.add(name);
	}

	public Set<String> getMates() {
		return mates;
	}

	@Override
	public int compareTo(Guest o) {
		if(this.getQty() > o.getQty()){
			return 1;
		}else if(this.getQty() < o.getQty()){
			return -1;
		}
		return 0;
	}

	
}
