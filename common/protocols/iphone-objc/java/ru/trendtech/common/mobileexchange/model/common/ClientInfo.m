#import "ClientInfo.h"

@implementation ClientInfo

@synthesize id;
@synthesize firstName;
@synthesize lastName;
@synthesize picure;
@synthesize country;
@synthesize city;
@synthesize phone;
@synthesize birthdayDay;
@synthesize birthdayMonth;
@synthesize birthdayYear;
@synthesize email;
@synthesize gender;
@synthesize rating;
@synthesize cards;
@synthesize password;
@synthesize picureUrl;

- (void) init {
  if (self = [super init]) {
    cards = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [firstName release];
  [lastName release];
  [picure release];
  [picureUrl release];
  [country release];
  [city release];
  [phone release];
  [email release];
  [gender release];
  [cards release];
  [password release];
  [super dealloc];
}

@end
