package Commands;

import tagger.TagRepository;

public class TaggingCommand implements Commands{
	
	private TagRepository theRepository;
	
	public TaggingCommand(TagRepository repository){//Ctor
		theRepository=repository;}
	
	private class addTag implements Command{


		public void execute() {
			//theRepository.addTag(..)
		}
	}
}
