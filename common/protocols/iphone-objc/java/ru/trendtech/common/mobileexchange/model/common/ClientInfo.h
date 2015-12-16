#import "CardInfo.h"
#import "NSString.h"
#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 * <p/>
 * <p/>
 * class represent all info about client
 */

@interface ClientInfo : NSObject {
  long id;
  NSString * firstName;
  NSString * lastName;
  NSString * picure;
  NSString * picureUrl;
  NSString * country;
  NSString * city;
  NSString * phone;
  int birthdayDay;
  int birthdayMonth;
  int birthdayYear;
  NSString * email;
  NSNumber * gender;
  int rating;
  NSMutableArray * cards;
  NSString * password;
}

@property(nonatomic) long id;
@property(nonatomic, retain) NSString * firstName;
@property(nonatomic, retain) NSString * lastName;
@property(nonatomic, retain) NSString * picure;
@property(nonatomic, retain) NSString * country;
@property(nonatomic, retain) NSString * city;
@property(nonatomic, retain) NSString * phone;
@property(nonatomic) int birthdayDay;
@property(nonatomic) int birthdayMonth;
@property(nonatomic) int birthdayYear;
@property(nonatomic, retain) NSString * email;
@property(nonatomic, retain) NSNumber * gender;
@property(nonatomic) int rating;
@property(nonatomic, retain) NSMutableArray * cards;
@property(nonatomic, retain) NSString * password;
@property(nonatomic, retain) NSString * picureUrl;
- (void) init;
- (void) setId:(long)id;
- (void) setFirstName:(NSString *)firstName;
- (void) setLastName:(NSString *)lastName;
- (void) setPicure:(NSString *)picure;
- (void) setCountry:(NSString *)country;
- (void) setCity:(NSString *)city;
- (void) setPhone:(NSString *)phone;
- (void) setBirthdayDay:(int)birthdayDay;
- (void) setBirthdayMonth:(int)birthdayMonth;
- (void) setBirthdayYear:(int)birthdayYear;
- (void) setEmail:(NSString *)email;
- (void) setGender:(NSNumber *)gender;
- (void) setRating:(int)rating;
- (void) setCards:(NSMutableArray *)cards;
- (void) setPassword:(NSString *)password;
- (void) setPicureUrl:(NSString *)picureUrl;
@end
